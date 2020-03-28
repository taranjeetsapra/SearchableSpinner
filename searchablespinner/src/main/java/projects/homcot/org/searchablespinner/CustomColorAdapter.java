package projects.homcot.org.searchablespinner;

import android.content.Context;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Taranjeet Singh on 30-Apr-19.
 */
public class CustomColorAdapter extends ArrayAdapter {
    private TextView tvSearchItemLabel;
    private TextView tvSearchItem;
    private LinearLayout llItem;
    private int rowSizeSp;
    private int dropDownColor;
    private int rowMargin;
    private ArrayList<String> arrayList;
    private int layout;
    private ListFilter listFilter = new ListFilter();
    private ArrayList<String> dataListAllItems;


    public CustomColorAdapter(@NonNull Context context, int layout, ArrayList<String> arrayList, int rowSizeSp,
                              int dropDownColor, int rowMargin) {
        super(context, layout);
        this.arrayList = arrayList;
        this.rowSizeSp = rowSizeSp;
        this.dropDownColor = dropDownColor;
        this.rowMargin = rowMargin;
        this.layout = layout;
    }

   /* public CustomColorAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        initializeViews(parent.getContext(),convertView, position);
        return convertView;
    }

    private void initializeViews(Context context, View view, int position) {
        tvSearchItemLabel = view.findViewById(R.id.tvSearchItemLabel);
        tvSearchItem = view.findViewById(R.id.tvSearchItem);
        llItem = view.findViewById(R.id.llItem);

        if (tvSearchItem != null) {
            tvSearchItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, rowSizeSp);
            tvSearchItem.setText(arrayList.get(position));

            llItem.setBackgroundColor(dropDownColor);
            // parent.setLayoutParams(new LayoutParams(200,200));
            ViewGroup.LayoutParams params = tvSearchItem.getLayoutParams();
            params.height = rowMargin;
            tvSearchItem.setLayoutParams(params);

        }
        if(position == 0){
            //tvSearchItemLabel.setVisibility(View.INVISIBLE);
            tvSearchItemLabel.setBackground(context.getResources().getDrawable(R.drawable.circle_white));
        }
        if(position == 1){
            tvSearchItemLabel.setBackground(context.getResources().getDrawable(R.drawable.circle_green));
        }else if(position == 2){
            tvSearchItemLabel.setBackground(context.getResources().getDrawable(R.drawable.circle_blue));
        }else if(position == 3){
            tvSearchItemLabel.setBackground(context.getResources().getDrawable(R.drawable.circle_yellow));
        }else if(position == 4){
            tvSearchItemLabel.setBackground(context.getResources().getDrawable(R.drawable.circle_red));
        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

   /* @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }*/

    @Nullable
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<String>(arrayList);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                arrayList = (ArrayList<String>) results.values;
            } else {
                arrayList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
