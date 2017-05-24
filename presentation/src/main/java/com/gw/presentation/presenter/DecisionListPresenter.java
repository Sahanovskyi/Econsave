package com.gw.presentation.presenter;

import com.gw.domain.exception.DefaultErrorBundle;
import com.gw.domain.exception.ErrorBundle;
import com.gw.domain.interactor.DefaultObserver;
import com.gw.domain.interactor.GetDecisionList;
import com.gw.domain.model.DecisionItem;
import com.gw.presentation.exception.ErrorMessageFactory;
import com.gw.presentation.internal.di.PerActivity;
import com.gw.presentation.mapper.DecisionModelDataMapper;
import com.gw.presentation.model.DecisionModel;
import com.gw.presentation.view.DecisionListView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by vadym on 09.05.17.
 */
@PerActivity
public class DecisionListPresenter implements Presenter {

    private DecisionListView decisionListView;

    private GetDecisionList getDecisionList;
    private DecisionModelDataMapper decisionModelDataMapper;

    @Inject
    public DecisionListPresenter(GetDecisionList getDecisionList, DecisionModelDataMapper decisionModelDataMapper) {
        this.getDecisionList = getDecisionList;
        this.decisionModelDataMapper = decisionModelDataMapper;
    }

    public void setView(DecisionListView decisionListView){
        this.decisionListView = decisionListView;
    }
    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.getDecisionList.dispose();
        this.decisionListView = null;

    }

    public void onDecisionClicked(DecisionModel decisionModel) {
     //   this.decisionListView.viewDecision(decisionModel);
    }

    /**
     * Initializes the presenter by start retrieving the decision list.
     */
    public void initialize() {
        this.loadDecisionList();
    }
    /**
     * Loads all decisions.
     */
    private void loadDecisionList() {
        this.getDecisionList();
    }


    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.decisionListView.context(),
                errorBundle.getException());
        this.decisionListView.showError(errorMessage);
    }

    private void showDecisionsCollectionInView(Collection<DecisionItem> decisionsCollection) {
         Collection<DecisionModel> decisionModelsCollection =
                this.decisionModelDataMapper.transform(decisionsCollection);
        Collections.sort((List<DecisionModel>)decisionModelsCollection);
        this.decisionListView.renderDecisionList(decisionModelsCollection);
    }

    private void getDecisionList() {
        this.getDecisionList.execute(new DecisionListObserver(), null);
        
    }

    private final class DecisionListObserver extends DefaultObserver<List<DecisionItem>> {

        @Override public void onComplete() {
        //    DecisionListPresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            DecisionListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override public void onNext(List<DecisionItem> decisionItems) {
            DecisionListPresenter.this.showDecisionsCollectionInView(decisionItems);
        }
    }
}
