package com.gw.presentation.mapper;

import com.gw.domain.model.DecisionItem;
import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.model.DecisionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

@PerActivity
public class DecisionModelDataMapper {

    @Inject
    public DecisionModelDataMapper() {
    }

    /**
     * Transform a {@link DecisionItem} into an {@link DecisionModel}.
     *
     * @param decisionItem Object to be transformed.
     * @return {@link DecisionModel}.
     */
    public DecisionModel transform(DecisionItem decisionItem) {
        if (decisionItem == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final DecisionModel decisionModel = new DecisionModel(decisionItem.getId(), decisionItem.getTitle());
        decisionModel.setTags(decisionItem.getTags());

        return decisionModel;
    }

    /**
     * Transform a Collection of {@link DecisionItem} into a Collection of {@link DecisionModel}.
     *
     * @param decisionsCollection Objects to be transformed.
     * @return List of {@link DecisionModel}.
     */
    public Collection<DecisionModel> transform(Collection<DecisionItem> decisionsCollection) {
        Collection<DecisionModel> decisionModelsCollection;

        if (decisionsCollection != null && !decisionsCollection.isEmpty()) {
            decisionModelsCollection = new ArrayList<>();
            for (DecisionItem decisionItem : decisionsCollection) {
                decisionModelsCollection.add(transform(decisionItem));
            }
        } else {
            decisionModelsCollection = Collections.emptyList();
        }

        return decisionModelsCollection;
    }
}
