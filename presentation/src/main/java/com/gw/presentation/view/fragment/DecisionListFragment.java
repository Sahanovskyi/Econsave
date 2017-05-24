package com.gw.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gw.presentation.R;
import com.gw.presentation.internal.di.component.DecisionComponent;
import com.gw.presentation.model.DecisionModel;
import com.gw.presentation.presenter.DecisionListPresenter;
import com.gw.presentation.view.DecisionListView;
import com.gw.presentation.view.adapters.DecisionsAdapter;
import com.gw.presentation.view.adapters.DecisionsLayoutManager;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DecisionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DecisionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DecisionListFragment extends BaseFragment implements DecisionListView {

    @Inject
    DecisionListPresenter decisionListPresenter;
    @Inject
    DecisionsAdapter decisionsAdapter;

    @BindView(R.id.rv_decisions)
    RecyclerView rv_decisions;
    private List<DecisionModel> decisionModelList;
    private DecisionListListener decisionListListener;
    private DecisionsAdapter.OnItemClickListener onItemClickListener =
            new DecisionsAdapter.OnItemClickListener() {
                @Override
                public void onDecisionItemClicked(DecisionModel DecisionModel) {
                    if (DecisionListFragment.this.decisionListPresenter != null && DecisionModel != null) {
                        DecisionListFragment.this.decisionListPresenter.onDecisionClicked(DecisionModel);
                    }
                }
            };

    public DecisionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DecisionListFragment.
     */
    public static DecisionListFragment newInstance() {
        DecisionListFragment fragment = new DecisionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(DecisionComponent.class).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decision_list, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.decisionListPresenter.setView(this);
        //   if (savedInstanceState == null) {
        this.loadDecisionList();
        //   }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.decisionListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.decisionListPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rv_decisions.setAdapter(null);
        //     ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.decisionListPresenter.destroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DecisionListListener) {
            this.decisionListListener = (DecisionListListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.decisionListListener = null;
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }


    @Override
    public void renderDecisionList(Collection<DecisionModel> decisionModelCollection) {
        if (decisionModelCollection != null) {
            this.decisionsAdapter.setDecisionsCollection(decisionModelCollection);
        }
    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    private void setupRecyclerView() {
        this.decisionsAdapter.setOnItemClickListener(onItemClickListener);
        this.rv_decisions.setLayoutManager(new DecisionsLayoutManager(context()));
        this.rv_decisions.setAdapter(decisionsAdapter);
    }


    /**
     * Loads all users.
     */
    private void loadDecisionList() {
        this.decisionListPresenter.initialize();
    }

    //   private OnFragmentInteractionListener mListener;
    public interface DecisionListListener {
        void onDecisionClicked(final DecisionModel decisionModel);
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    *
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
