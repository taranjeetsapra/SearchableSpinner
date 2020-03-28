package projects.homcot.org.searchablespinner;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;

import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class SearchableSpinner extends LinearLayout {

    public static final String ACTION_NEXT = "0";
    public static final String ACTION_DONE = "1";
    private SearchableSpinnerInterface searchableSpinnerInterface;
    private Context context;
    private View view;
    private boolean isSearchableSpinnerVisibility = true;
    private String selectedItem = "";
    private float searchableSpinnerTextSize;
    private String strSearchableSpinnerInputType;
    public static final String TEXT_PASSWORD = "0";
    public static final String NUMBER_PASSWORD = "1";
    public static final String TEXT = "2";
    public static final String TEXT_EMAIL_ADDRESS = "3";
    public static final String NUMBER = "4";
    public static final String PHONE = "5";
    public static final String NUMBER_DECIMAL = "6";
    private String strSearchableSpinnerImeOptions;

    public SearchableSpinner(Context context) {
        super(context);
        initializeViews(context, null);
    }


    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {

        try {
            this.context = context;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
            if (a == null) {
                return;
            }
            searchableSpinnerTextSize = a.getDimension(R.styleable.SearchableSpinner_searchableSpinnerTextSize, dp2px(context, 18));//Done
            strSearchableSpinnerInputType = a.getString(R.styleable.SearchableSpinner_searchableSpinnerInputType);
            if (strSearchableSpinnerInputType != null) {
                if (strSearchableSpinnerInputType.equals(""))
                    strSearchableSpinnerInputType = "2";
            } else {
                strSearchableSpinnerInputType = "2";
            }
            strSearchableSpinnerImeOptions = a.getString(R.styleable.SearchableSpinner_searchableSpinnerImeOptions);

            setOrientation(LinearLayout.VERTICAL);
            setGravity(Gravity.CENTER_VERTICAL);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.searchable_spinner, this, true);
            a.recycle();

            setOnclickForView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setOnclickForView() {
        LinearLayout llImageView = view.findViewById(R.id.llImageSearch);
        final ImageView imageSearch = findViewById(R.id.imageSearch);
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        llImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageSearch.getTag().toString().equals("down")) {
                    imageSearch.setTag("up");
                    autoCompleteTextView.setHint("Search..");
                    imageSearch.setImageDrawable(getResources().getDrawable(R.drawable.up_arrow_white));
                    //  autoCompleteTextView.setEnabled(true);
                    autoCompleteTextView.showDropDown();
                } else {
                    imageSearch.setTag("down");
                    // autoCompleteTextView.setEnabled(false);
                    autoCompleteTextView.setHint("No Selection");
                    imageSearch.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow_white));
                    autoCompleteTextView.dismissDropDown();
                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (imageSearch.getTag().toString().equals("up")) {
                    imageSearch.setTag("down");
                    // autoCompleteTextView.setEnabled(false);
                    autoCompleteTextView.setHint("No Selection");
                    imageSearch.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow_white));
                    autoCompleteTextView.dismissDropDown();
                }

                Object item = adapterView.getItemAtPosition(position);
                selectedItem = item.toString();
                if (selectedItem == null)
                    selectedItem = "";
                searchableSpinnerInterface.setSpinnerOnClickListener(view, item.toString());

            }
        });

    }


    public void setSearchableSpinnerVisibility(boolean isSearchableSpinnerVisibility) {
        LinearLayout linearLayout = view.findViewById(R.id.searchableSpinner);
        if (isSearchableSpinnerVisibility)
            linearLayout.setVisibility(VISIBLE);
        else
            linearLayout.setVisibility(GONE);
    }


    public void setSearchableSpinnerThreshold(int threshold) {
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setThreshold(threshold);
    }

    public void setSearchableSpinnerDropDownHeight(int height) {
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setDropDownHeight(height);
    }

    private void setSearchableSpinnerAdapter(ArrayAdapter adapter) {
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);

    }

    public void setSearchableSpinnerDropDownWidth(int width) {
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setDropDownWidth(width);
    }

    public void setSearchableSpinnerColorAdapter(Activity context, ArrayList<String> arrayList, final int rowSizeSp,
                                                 final int dropDownColor, final int rowMargin) {

        CustomColorAdapter customColorAdapter=new CustomColorAdapter(context, R.layout.color_dialog_blue_item,
                arrayList,  rowSizeSp, dropDownColor, rowMargin);
      /*  AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(customColorAdapter);*/

        setSearchableSpinnerAdapter(customColorAdapter);

    }

    public void setSearchableSpinnerAdapter(Activity context, ArrayList<String> arrayList, final int rowSizeSp,
                                            final int dropDownColor, final int rowMargin) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, arrayList) {
            //Overrride this only for most of the cases
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                TextView row = (TextView) super.getView(position, convertView, parent);
                row.setTextSize(TypedValue.COMPLEX_UNIT_SP, rowSizeSp);
                parent.setBackgroundColor(dropDownColor);
                // parent.setLayoutParams(new LayoutParams(200,200));
                ViewGroup.LayoutParams params = row.getLayoutParams();
                params.height = rowMargin;
                row.setLayoutParams(params);
                return row;
                //return super.getView(position, convertView, parent);

            }
             /*//Use this when want different values shown at dropdown then after selection
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    return super.getDropDownView(position, convertView, parent);
                }*/
        };

        setSearchableSpinnerAdapter(adapter);
    }
    //setSearchableSpinnerAdapter

/*

    @Override
    public void setOnClickListener(OnClickListener onclickListener) {
       */
/* AutoCompleteTextView autoCompleteTextView=view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setOnClickListener(onclickListener);*//*


    }
*/


    public void performSearchableSpinnerClick() {
        LinearLayout llImageView = view.findViewById(R.id.llImageSearch);
        llImageView.performClick();
    }

    public void resetSearchableSpinnerClick() {

        LinearLayout llImageView = view.findViewById(R.id.llImageSearch);
        final ImageView imageSearch = findViewById(R.id.imageSearch);
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        if (!imageSearch.getTag().toString().equals("down")) {
            imageSearch.setTag("down");
            autoCompleteTextView.setHint("No Selection");
            imageSearch.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow_white));
            autoCompleteTextView.dismissDropDown();
        }
    }

    public void disableSearchableSpinnerClick() {
        LinearLayout linearLayout = view.findViewById(R.id.searchableSpinner);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setEnabled(false);
        LinearLayout llImageSearch = view.findViewById(R.id.llImageSearch);
        llImageSearch.setEnabled(false);
        ImageView imageSearch = view.findViewById(R.id.imageSearch);
        imageSearch.setEnabled(false);
        linearLayout.setEnabled(false);
    }

    public void enableSearchableSpinnerClick() {
        LinearLayout linearLayout = view.findViewById(R.id.searchableSpinner);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setEnabled(true);
        LinearLayout llImageSearch = view.findViewById(R.id.llImageSearch);
        llImageSearch.setEnabled(true);
        ImageView imageSearch = view.findViewById(R.id.imageSearch);
        imageSearch.setEnabled(true);
        linearLayout.setEnabled(true);
    }

    public String getSearchableSpinnerText() {
        String str = "";
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        if (autoCompleteTextView.getText() != null) {
            str = autoCompleteTextView.getText().toString();
        }
        return str;
    }

    public void setSearchableSpinnerText(String str) {
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        if (autoCompleteTextView.getText() != null) {
            autoCompleteTextView.setText(str);
        }
    }

    public void setSpinnerOnClickListener(SearchableSpinnerInterface searchableSpinnerInterface) {
        this.searchableSpinnerInterface = searchableSpinnerInterface;
    }

    public interface SearchableSpinnerInterface {
        void setSpinnerOnClickListener(View view, String selectedTxt);

    }

    /**
     * Pass searchableSpinnerTextSize value in sp
     *
     * @param searchableSpinnerTextSize
     */
    public void setSearchableSpinnerTextSize(float searchableSpinnerTextSize) {
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        try {
            setSearchableSpinnerTextSize(searchableSpinnerTextSize);
            if (strSearchableSpinnerInputType != null) {
                if (!strSearchableSpinnerInputType.isEmpty())
                    setInputType(strSearchableSpinnerInputType);
            }

            if (strSearchableSpinnerImeOptions != null) {
                if (!strSearchableSpinnerImeOptions.isEmpty())
                    setUpImeOptions(strSearchableSpinnerImeOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpImeOptions(String strSearchableSpinnerImeOptions) {
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        switch (strSearchableSpinnerImeOptions) {
            case ACTION_NEXT: {
                autoCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                break;
            }
            case ACTION_DONE: {
                autoCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                break;
            }
        }

    }


    private void setInputType(String strSearchableSpinnerInputType) {
        AutoCompleteTextView editText = view.findViewById(R.id.autoCompleteTextView);

        switch (strSearchableSpinnerInputType) {
            case TEXT_PASSWORD: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            }
            case NUMBER_PASSWORD: {
                editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            }
            case TEXT: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            }
            case TEXT_EMAIL_ADDRESS: {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            }
            case NUMBER: {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            }
            case PHONE: {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            }
            case NUMBER_DECIMAL: {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            }
        }
    }
}

