package com.example.dylhunn.netflixchillandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import com.example.dylhunn.netflixchillandroid.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<String> items = new ArrayList<>();

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    // a mapping from positions in the listview to the person at that position
    // if that position is a menu, title, or header, person is null
    public Map<Integer, Person> listIndicesMap;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, items);
        listIndicesMap = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        ApiService.fetchMatches(ChillActivity.uid, this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * Receives a List of ChillRequestResponses, each of which knows its ID and data. Each such
     * response is itself a list of user IDs who are matched for that request.
     */
    public void populate(List<ChillRequestResponseList<Person>> matches) {

        // sort by "priority" for chronological order
        Collections.sort(matches, new Comparator<ChillRequestResponseList<Person>>() {
            @Override
            public int compare(ChillRequestResponseList<Person> lhs, ChillRequestResponseList<Person> rhs) {
                return lhs.DATA.PRIORITY - rhs.DATA.PRIORITY;
            }
        });

        listIndicesMap = new HashMap<>();

        int currentIndex = 0;

        for (int i = 0; i < matches.size(); i++) {

            ChillRequestResponseList<Person> sublist = matches.get(i);

            // Sort people by matching score! Higher is better.
            Collections.sort(sublist, new Comparator<Person>() {
                @Override
                public int compare(Person lhs, Person rhs) {
                    return (rhs.PRIORITY - lhs.PRIORITY) > 0 ? 1 : -1;
                }
            });

            String heading = sublist.DATA.GENRE;
            if (sublist.DATA.TYPE == ChillRequest.MediaType.FILM)
                    heading += " Movie ";
            else heading += " TV Show ";
            heading += "on ";
            heading += sublist.DATA.DAY;
            heading += " ";
            heading += sublist.DATA.TIME;

            items.add(heading);
            listIndicesMap.put(currentIndex, null);
            currentIndex++;

            int j;
            for (j = 0; j < sublist.size(); j++) {
                items.add("   " + sublist.get(j).NAME);
                listIndicesMap.put(currentIndex, sublist.get(j));
                currentIndex++;
            }
            if (j == 0) {
                items.add("   No matches yet");
                listIndicesMap.put(currentIndex, null);
                currentIndex++;
            }
        }

        if (currentIndex == 0) { // we are still empty :(
           // showProgress(true);

            items.add("No matches yet.");
        } else { // not empty!
           // showProgress(false);
        }

        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        final View mProgressView = getView().findViewById(R.id.login_progress);
        final View mTextView = getView().findViewById(R.id.comeBackView);


        // On Honeycomb MR2 we have th\e ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            mTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mTextView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
