package com.gw.data.entity.mapper;

import com.gw.data.entity.TransactionItemEntity;
import com.gw.domain.exception.BankException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Class used to transform from Document to valid objects.
 */
@Singleton
public class TransactionItemEntityXMLMapper {

    @Inject
    public TransactionItemEntityXMLMapper() {
    }

    /**
     * Transform from Document to List of {@link TransactionItemEntity}.
     *
     * @param response A String representing a collection of transactions.
     * @return List of {@link TransactionItemEntity}.
     * @throws BankException  if the PrivatBank provided a transaction history.
     * @throws ParseException if the Document is not a valid XML structure.
     */
    public List<TransactionItemEntity> transformTransactionEntityCollection(String response)
            throws BankException, ParseException, ParserConfigurationException, SAXException, IOException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(response));

        Document docResponse = db.parse(is);
        docResponse.normalizeDocument();
        NodeList nodeListError = docResponse.getElementsByTagName("error");
        if (nodeListError.getLength() != 0) {
            for (int i = 0; i < nodeListError.getLength(); i++) {
                Node node = nodeListError.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node message = attributes.getNamedItem("message");
                String errorMsg = message.getNodeValue();
                throw new BankException(errorMsg);
            }
        } else {
            NodeList nodeList = docResponse.getElementsByTagName("statement");
            ArrayList<TransactionItemEntity> items = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap attributes = node.getAttributes();

                Node cardAmount = attributes.getNamedItem("cardamount");
                String cardAmountNodeValue = cardAmount.getNodeValue();
                Double amount = Double.parseDouble(cardAmountNodeValue.split(" ")[0]);

                String currency = cardAmountNodeValue.split(" ")[1];

                Node restNode = attributes.getNamedItem("rest");
                String restValue = restNode.getNodeValue();
                Double rest = Double.parseDouble(restValue.split(" ")[0]);

                Node terminal = attributes.getNamedItem("terminal");
                String terminalNodeValue = terminal.getNodeValue();

                Node tranDate = attributes.getNamedItem("trandate");
                String tranDateNodeValue = tranDate.getNodeValue();

                Node tranTime = attributes.getNamedItem("trantime");
                String tranTimeNodeValue = tranTime.getNodeValue();

                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dt.parse(tranDateNodeValue + " " + tranTimeNodeValue);


                items.add(new TransactionItemEntity(amount, rest, date, terminalNodeValue, currency));
            }
            return items;
        }
        return null;
    }

}
