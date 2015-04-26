package com.place.mania;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

public abstract class SearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener { //, TeeweScrollView.OnBottomReachedListener {

//    //******************************* Global Constants & Properties ********************************//
//
//    private LinearLayout sectionsContainer;
//    private FrameLayout loadingLayout;
//    private LinearLayout loadingMoreLayout;
//    private TeeweScrollView scrollView;
//    private ArrayList<ExpandableListSection> sections = new ArrayList<>();
//    protected String LOGTAG = this.getClass().getSimpleName();
//    private final String TAG = "TeeweSearchFragment";
//    private View.OnClickListener onClickListener;
//    private AdapterView.OnItemClickListener onItemClickListener;
//
//    // Key = List ID, Value = Section Number;
//    private HashMap<Integer, Integer> listToSectionIdMap = new HashMap<>();
//
////******************************* Abstract Methods ********************************//
//
//    protected abstract int numberOfSections();
//
//    protected abstract String getSectionTitle(int sectionNumber);
//
//    protected abstract void showMoreClicked(int sectionNumber);
//
//    protected abstract int getListItemsSizeForSection(int sectionNumber);
//
//    protected abstract int numberOfItemsToShow(int sectionNumber);
//
//    protected abstract void onSectionListUpdated(int sectionNumber);
//
//    public abstract void doSearchForQuery(String query);
//
//    public abstract ExpandableListBaseAdapter getExpandableListAdapter(int sectionNumber);
//
//    public abstract void onListItemClicked(int sectionNumber, int position);
//
//    public abstract void onListScrolledToBottom(int sectionNumber);
//
//    public abstract boolean showLoadMoreButton(int sectionNumber);
//
//    protected abstract Object getListItemsForSection(int sectionNumber);
//
//    protected abstract boolean showLoadingMoreSpinner(int sectionNumber);
//
////********************************** Public Getters & Setters **********************************//
////*************************************** Constructor(s) ***************************************//
////********************************** Protected Methods *****************************************//
//
//    protected void showLoadingLayout(final boolean show) {
//        if (getActivity() != null)
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (show) {
//                        loadingLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        loadingLayout.setVisibility(View.GONE);
//                    }
//                }
//            });
//    }
//
//    protected void showMoreItems(int sectionNumber, int numberOfItemsToShow) {
//        sections.get(sectionNumber).getListAdapter().showMoreItems(numberOfItemsToShow);
//        sections.get(sectionNumber).getListView().refreshListViewHeight();
//        onSectionListUpdated(sectionNumber);
//    }
//
//    protected void showAllItems(int sectionNumber) {
//        sections.get(sectionNumber).getListAdapter().showAllItems();
//        sections.get(sectionNumber).getListView().refreshListViewHeight();
//        onSectionListUpdated(sectionNumber);
//    }
//
//    protected void setAllLoaded(final int sectionNumber) {
//
//        if (sections.isEmpty())
//            return;
//
//        if (getActivity() != null)
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    sections.get(sectionNumber).getExpandButtonLayout().setVisibility(View.GONE);
//                    sections.get(sectionNumber).getListView().removeFooterView(loadingMoreLayout);
//                }
//            });
//    }
//
//    protected int getSectionItemCount(int sectionNumber) {
//        return sections.get(sectionNumber).getListAdapter().getCount();
//    }
//
//    protected void refreshSection(final int sectionNumber) {
//
//        if (sections.isEmpty())
//            return;
//
//        if (sectionNumber < 0 || sectionNumber >= sections.size())
//            return;
//
//        if (getActivity() != null)
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (getListItemsSizeForSection(sectionNumber) == 0) {
//                        LogTW.d(LOGTAG, "List is empty");
//                        sections.get(sectionNumber).getListView().setVisibility(View.GONE);
//                        sections.get(sectionNumber).getExpandButtonLayout().setVisibility(View.GONE);
//                    } else {
//                        LogTW.d(LOGTAG, "List is not empty");
//                        sections.get(sectionNumber).getListView().setVisibility(View.VISIBLE);
//                        sections.get(sectionNumber).getExpandButtonLayout().setVisibility(View.VISIBLE);
//                        sections.get(sectionNumber).getListAdapter().setDataSource(getListItemsForSection(sectionNumber));
//                        sections.get(sectionNumber).getListAdapter().showItems(numberOfItemsToShow(sectionNumber));
//                        sections.get(sectionNumber).getListView().refreshListViewHeight();
//                    }
//                    onSectionListUpdated(sectionNumber);
//                }
//            });
//    }
//
//    protected void refreshAllSections() {
//        for (int i = 0; i < numberOfSections(); i++) {
//            refreshSection(i);
//        }
//    }
//
////************************************* Interfaces *********************************************//
////************************************* Private Methods ****************************************//
//
//    protected void createSections() {
//
//        onClickListener = this;
//        onItemClickListener = this;
//        sections.clear();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                for (int i = 0; i < numberOfSections(); i++) {
//                    LinearLayout sectionLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.search_results_section, sectionsContainer, false);
//                    ExpandableListSection section = new ExpandableListSection(i, sectionLayout, getExpandableListAdapter(i));
//                    section.getExpandButtonLayout().setOnClickListener(onClickListener);
//                    sections.add(section);
//                }
//
//                createViews();
//            }
//        }).start();
//    }
//
//    protected void createViews() {
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    for (ExpandableListSection section : sections) {
//                        section.getTitleTextView().setText(getSectionTitle(sections.indexOf(section)));
//                        section.getListView().setOnItemClickListener(onItemClickListener);
//                        section.getListView().setAdapter(section.getListAdapter());
//                        if (numberOfItemsToShow(sections.indexOf(section)) == 0)
//                            section.getExpandButtonLayout().setVisibility(View.GONE);
//
//                        if (showLoadMoreButton(sections.indexOf(section)))
//                            section.getExpandButtonLayout().setVisibility(View.VISIBLE);
//                        else
//                            section.getExpandButtonLayout().setVisibility(View.GONE);
//
//                        if (showLoadingMoreSpinner(sections.indexOf(section))) {
//                            LogTW.d(LOGTAG, "Adding footer layout ");
//                            loadingMoreLayout.setVisibility(View.VISIBLE);
//                            sections.get(sections.indexOf(section)).getListView().addFooterView(loadingMoreLayout);
//                        } else {
//                            LogTW.d(LOGTAG, "not adding footer layout ");
//                        }
//
//                        if (section.getSectionLayout().getParent() == null)
//                            sectionsContainer.addView(section.getSectionLayout());
//                    }
//                }
//            });
//        } else {
//            LogTW.e(LOGTAG, "getActivity is null");
//        }
//    }
//
//    protected final class ExpandableListSection {
//
//        private NotScrollingListView listView;
//        private TextView titleTextView;
//        private RelativeLayout expandButtonLayout;
//        private int sectionNumber;
//        private LinearLayout sectionLayout;
//        private ExpandableListBaseAdapter listAdapter;
//
//        public ExpandableListSection(int sectionNumber, LinearLayout sectionLayout, ExpandableListBaseAdapter listAdapter) {
//            this.sectionLayout = sectionLayout;
//            this.listView = (NotScrollingListView) sectionLayout.findViewById(R.id.search_results_list);
//            this.listView.setId(Utils.generateViewId());
//            listToSectionIdMap.put(listView.getId(), sectionNumber);
//            this.titleTextView = (TextView) sectionLayout.findViewById(R.id.section_title);
//            this.expandButtonLayout = (RelativeLayout) sectionLayout.findViewById(R.id.search_expand_list_layout);
//            this.expandButtonLayout.setId(Utils.generateViewId());
//            this.sectionNumber = sectionNumber;
//            this.listAdapter = listAdapter;
//
//            LogTW.d(LOGTAG, "Section id : " + sectionNumber);
//
//        }
//
//        public NotScrollingListView getListView() {
//            return listView;
//        }
//
//        public TextView getTitleTextView() {
//            return titleTextView;
//        }
//
//        public RelativeLayout getExpandButtonLayout() {
//            return expandButtonLayout;
//        }
//
//        public int getSectionNumber() {
//            return sectionNumber;
//        }
//
//        public LinearLayout getSectionLayout() {
//            return sectionLayout;
//        }
//
//        public ExpandableListBaseAdapter getListAdapter() {
//            return listAdapter;
//        }
//
//        public void setListAdapter(ExpandableListBaseAdapter adapter) {
//            this.listAdapter = adapter;
//        }
//    }
//
////************************************* Implemented Methods ************************************//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.search_frag_new, container, false);
//        sectionsContainer = (LinearLayout) view.findViewById(R.id.search_section_container);
//        loadingLayout = (FrameLayout) view.findViewById(R.id.loading_layout);
//        loadingMoreLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.listview_loadmore_layout, null);
//        scrollView = (TeeweScrollView) view.findViewById(R.id.search_scrollview);
//        scrollView.setOnBottomReachedListener(this);
//        loadingLayout.setVisibility(View.GONE);
//        createSections();
//        LogTW.d(LOGTAG, "onCreateView");
//        return view;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        onClickListener = this;
//        LogTW.d(LOGTAG, "onCreate");
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onClick(View v) {
//        for (ExpandableListSection section : sections)
//            if (v.getId() == section.getExpandButtonLayout().getId())
//                showMoreClicked(sections.indexOf(section));
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        onListItemClicked(listToSectionIdMap.get(parent.getId()), position);
//    }
//
//    @Override
//    public void onBottomReached() {
//        onListScrolledToBottom(numberOfSections() - 1);
//    }
}
