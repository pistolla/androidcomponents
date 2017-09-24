package com.eddmash.adapter;
/*
* This file is part of the com.eddmash.adapter package.
* 
* (c) Eddilbert Macharia (http://eddmash.com)<edd.cowan@gmail.com>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.eddmash.pagination.PaginatorInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * An adapter whose data can filtered and also if used together with classes that implement
 * PaginatorInterface can be paginated.
 *
 * @param <VH>
 */
public abstract class BaseFiltarableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements SqlPageableAdaptorInterface {

    private AppCompatActivity activity;
    List<Map> mData;

    /**
     * @return the current dataset the adapter is working on.
     */
    public List<Map> getData() {
        return filterdData;
    }

    private List<Map> filterdData;
    private int itemLayout;

    public BaseFiltarableAdapter(AppCompatActivity activity) {
        this.activity = activity;
        filterdData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return filterdData.size();
    }

    @Override
    public void addAll(List<Map> data) {
        clear();
        this.filterdData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(PaginatorInterface paginator) {
        addAll(paginator.getData());
    }

    @Override
    public void add(Map data) {
        this.filterdData.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void update(List<Map> maps) {
        List<Map> records = new ArrayList<>(maps);
        int size = this.filterdData.size();
        for (Map data : records) {
            this.filterdData.add(data);
        }
        notifyItemRangeInserted(size, records.size());
    }

    @Override
    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        filterdData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void filter(String searchTerm) {
        mData = filterdData;
        if (!searchTerm.trim().isEmpty()) {
            filterdData = doFilter(searchTerm, filterdData);
        }
        notifyDataSetChanged();
    }

    /**
     * Implement method to add logic to be used when searching for record(s) within the current
     * dataset  the adapter is handling.
     * <p>
     * You just need to filter through the records in the haystack and return the new values
     * to display.
     * <p>
     * NB::If you wish to perform searched that hit the database consider using the PaginatorInterface
     * together with concrete implementations of this class
     * <p>
     * return the haystack as is if no filtering is done.
     *
     * @param searchTerm the search value
     * @param hayStack   the records to search the term in
     */
    protected abstract List<Map> doFilter(String searchTerm, List<Map> hayStack);


}
