package com.gw.presentation.view;

import com.gw.presentation.model.DecisionModel;

import java.util.Collection;

/**
 * Created by vadym on 26.04.17.
 */

public interface DecisionListView extends View{

    /**
     * Render a decision list in the UI.
     *
     * @param decisionModelCollection The collection of {@link DecisionModel} that will be shown.
     */
    void renderDecisionList(Collection<DecisionModel> decisionModelCollection);


}
