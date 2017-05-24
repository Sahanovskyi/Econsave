package com.gw.presentation.mapper;

import com.gw.data.entity.TransactionItemEntity;
import com.gw.domain.model.TransactionItem;
import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.model.TransactionItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;


@PerActivity
public class TransactionItemModelMapper {

    @Inject
    public TransactionItemModelMapper() {
    }

    /**
     * Transform a {@link TransactionItemEntity} into an {@link TransactionItem}.
     *
     * @param item Object to be transformed.
     * @return {@link TransactionItem} if valid {@link TransactionItemEntity} otherwise null.
     */
    public TransactionItemModel transform(TransactionItem item) {
        TransactionItemModel itemModel = null;
        if (item != null) {
            itemModel = new TransactionItemModel(item.getDate(), item.getAmount());
            itemModel.setBalance(item.getBalance());
            itemModel.setDescription(item.getDescription());
            itemModel.setCurrency(item.getCurrency());
        }
        return itemModel;
    }

    /**
     * Transform a List of {@link TransactionItemEntity} into a Collection of {@link TransactionItem}.
     *
     * @param itemCollection Object Collection to be transformed.
     * @return {@link TransactionItem} if valid {@link TransactionItemEntity} otherwise null.
     */
    public List<TransactionItemModel> transform(Collection<TransactionItem> itemCollection) {
        final List<TransactionItemModel> itemModels = new ArrayList<>();
        for (TransactionItem item : itemCollection) {
            final TransactionItemModel itemModel = transform(item);
            if (itemModel != null) {
                itemModels.add(itemModel);
            }
        }
        return itemModels;
    }

}
