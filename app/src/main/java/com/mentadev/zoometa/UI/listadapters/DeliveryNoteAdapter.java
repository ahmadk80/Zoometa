package com.mentadev.zoometa.UI.listadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.activities.DeliveryNoteDetailsActivity;
import com.mentadev.zoometa.UI.activities.LandingActivity;
import com.mentadev.zoometa.UI.activities.MainActivity;
import com.mentadev.zoometa.datamodel.DeliveryNoteDetails;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanning;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteAdapter extends RecyclerView.Adapter<DeliveryNoteAdapter.ViewHolder> {

    private List<DeliveryNoteScanning> deliveryNotesHistoryList;
    private Context context;

    public List<DeliveryNoteScanning> getDeliveryNotesHistoryList() {
        return deliveryNotesHistoryList;
    }

    public void setDeliveryNotesHistoryList(List<DeliveryNoteScanning> deliveryNotesHistoryList) {
        this.deliveryNotesHistoryList = deliveryNotesHistoryList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView listViewTxtDeliveryNoteId;
        private final TextView listViewTxtDeliveryNoteName;
        private final TextView listViewTxtDeliveryNoteScanningDate;
        private final LinearLayout list_delivery_notes_history_row;
        public ViewHolder(View view, Context context) {
            super(view);
          //  listViewTxtDeliveryNoteId =  view.findViewById(R.id.listViewTxtDeliveryNoteId);
            listViewTxtDeliveryNoteName =  view.findViewById(R.id.listViewTxtDeliveryNoteName);
            listViewTxtDeliveryNoteScanningDate =  view.findViewById(R.id.listViewTxtDeliveryNoteScanningDate);
            list_delivery_notes_history_row = view.findViewById(R.id.list_delivery_notes_history_row);
            view.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, DeliveryNoteDetailsActivity.class);
                intent.putExtra("ID", getListViewTxtDeliveryNoteName().getTag().toString());
                intent.putExtra("NAME", getListViewTxtDeliveryNoteName().getText().toString());
                context.startActivity(intent);
            });
        }

        public TextView getListViewTxtDeliveryNoteName() {
            return listViewTxtDeliveryNoteName;
        }
//
//        public TextView getListViewTxtDeliveryNoteId() {
//            return listViewTxtDeliveryNoteId;
//        }

        public TextView getListViewTxtDeliveryNoteScanningDate() {
            return listViewTxtDeliveryNoteScanningDate;
        }

        public LinearLayout getList_delivery_notes_history_row() {
            return list_delivery_notes_history_row;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public DeliveryNoteAdapter(List<DeliveryNoteScanning> dataSet, Context context) {
        setDeliveryNotesHistoryList(dataSet);
        setContext(context);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.delivery_note_history_list_row, viewGroup, false);

        return new ViewHolder(view, getContext());
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getListViewTxtDeliveryNoteName().setText(getDeliveryNotesHistoryList().get(position).getName());
        viewHolder.getListViewTxtDeliveryNoteName().setTag(getDeliveryNotesHistoryList().get(position).getDeliveryNoteId());
        if(position %2 == 1)
        {
            viewHolder.getList_delivery_notes_history_row().setBackground(ContextCompat.getDrawable(getContext(), R.drawable.delivery_note_history_table_borders_odd));
        }
        else
        {
            viewHolder.getList_delivery_notes_history_row().setBackground(ContextCompat.getDrawable(getContext(), R.drawable.delivery_note_history_table_borders_even));
        }
        viewHolder.getListViewTxtDeliveryNoteScanningDate().setText(getDeliveryNotesHistoryList().get(position).getScanningDateDisplay());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return getDeliveryNotesHistoryList().size();
    }
}