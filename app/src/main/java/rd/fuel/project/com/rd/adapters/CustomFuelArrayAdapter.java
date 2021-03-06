package rd.fuel.project.com.rd.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.personalproject.R;
import com.example.personalproject.adapters.CustomFuelAdapterItem.AdapterItemType;
import com.example.personalproject.combustible.Combustible;
import com.example.personalproject.utilitys.Utilitys;

import java.util.ArrayList;

public class CustomFuelArrayAdapter extends RecyclerView.Adapter<ViewHolder> {
    // Activity context
    private final Context mContext;
    // List of items that are shown in the assistance list view.
    private final ArrayList<CustomFuelAdapterItem> mAdapterList;

    /***
     * @param context
     * @param mAdapterList
     */
    public CustomFuelArrayAdapter(Context context, ArrayList<CustomFuelAdapterItem> mAdapterList) {
        this.mContext = context;
        this.mAdapterList = mAdapterList;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(viewHolder instanceof CombustibleViewHolder ){
            displayCombustibleRow((CombustibleViewHolder) viewHolder, position);

        } else if (viewHolder instanceof HeaderViewHolder){
            displayHeardViewRow((HeaderViewHolder) viewHolder, position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == AdapterItemType.COMBUSTIBLE_VIEW.ordinal()){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fuel_list_row, viewGroup, false);
            return new CombustibleViewHolder(view);

        } else if(viewType == AdapterItemType.HEADER_VIEW.ordinal()){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fuel_date_header_view, viewGroup, false);
            return new HeaderViewHolder(view);

        } else return null;
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == DownloadBillAdapterItemType.BILL_DETAILS_VIEW.ordinal()) {
//            return new BillDetailsViewHolder(mContext.getLayoutInflater().inflate(R.layout.download_bill_row_layout, parent, false));
//        } else if (viewType == DownloadBillAdapterItemType.HEADER_VIEW.ordinal()) {
//            return new HeaderViewHolder(mContext.getLayoutInflater().inflate(R.layout.list_view_informative_header_row_layout, parent, false));
//        } else {
//            // Unsupported view type.
//            return null;
//        }
//    }


    @Override
    public int getItemCount() {
        return mAdapterList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapterList.get(position).getAdapterItemType().ordinal();
    }

    /**
     * @param combustible
     * @return
     */
    private int getCorrespondingImage(Combustible combustible) {
        int imageById = 0;

        double lastPrice = combustible.getLastPrice();
        double currentPrice = combustible.getPrice();

        if (currentPrice > lastPrice) {
            // Price up
            imageById = R.drawable.arrow_up_red;

        } else if (currentPrice == lastPrice) {
            // Price equals
            imageById = R.drawable.arrow_ecuals_yellow;

        } else {
            // Price down
            imageById = R.drawable.arrow_down_green;
        }

        return imageById;
    }

    /**
     * @param combustible
     * @return
     */

    public double getDifferencePrice(Combustible combustible) {
        return (combustible.getPrice() - combustible.getLastPrice());
    }


    private void displayHeardViewRow(HeaderViewHolder viewHolder, int position){
        viewHolder.title.setText("Prueba Row title: 20-15-2016");
    }

    private void displayCombustibleRow(CombustibleViewHolder holder, int position){
        CustomFuelAdapterItem adapterItem = mAdapterList.get(position);
        Combustible combustible =  adapterItem.getCombustible();

        holder.description.setText(combustible.getDescription());
        holder.price.setText(Utilitys.setNumberFormatToShow(combustible.getPrice()));
        holder.differencePrice.setText(Utilitys.setNumberFormatToShow(getDifferencePrice(combustible)));
        holder.image.setImageResource(getCorrespondingImage(combustible));
    }

    /***
     * Create a holder Class to contain inflated xml file elements
     */
    public static class CombustibleViewHolder extends ViewHolder {
        protected TextView price;
        protected TextView differencePrice;
        protected TextView description;
        protected ImageView image;

        CombustibleViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.list_image);
            price = (TextView) view.findViewById(R.id.curren_price);
            description = (TextView) view.findViewById(R.id.description_title);
            differencePrice = (TextView) view.findViewById(R.id.difference_price_value);
        }
    }

    /***
     *
     */
    public static class HeaderViewHolder extends ViewHolder {
        TextView title;

        HeaderViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_header);
        }
    }

    /**
     *
     */
//    private class OnItemClickListener implements OnClickListener {
//        int position;
//
//        public OnItemClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//
//            FuelPriceActivity fuelPrice = (FuelPriceActivity) mContext;
//
//            /****
//             * Call onItemClick Method inside CustomListViewAndroidExample Class
//             * ( See Below )
//             ****/
//
//            fuelPrice.onItemClick(position);
//
//        }
//    }
}
