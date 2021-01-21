package com.mentadev.zoometa.UI.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mentadev.zoometa.R;
import com.mentadev.zoometa.datamodel.DeliveryNoteDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteDetailsAdapter extends RecyclerView.Adapter<DeliveryNoteDetailsAdapter.ViewHolder> implements Filterable {

    private List<DeliveryNoteDetails> deliveryNoteDetailsList;
    private List<DeliveryNoteDetails> deliveryNoteDetailsListFiltered;
    private Context context;
    public void setDeliveryNotesHistoryList(List<DeliveryNoteDetails> _deliveryNoteDetailsList) {
        if (this.deliveryNoteDetailsList == null) {
            deliveryNoteDetailsList = _deliveryNoteDetailsList;
            this.deliveryNoteDetailsListFiltered = _deliveryNoteDetailsList;
            notifyItemChanged(0, deliveryNoteDetailsListFiltered.size());
        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return DeliveryNoteDetailsAdapter.this.deliveryNoteDetailsList.size();
                }

                @Override
                public int getNewListSize() {
                    return deliveryNoteDetailsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return DeliveryNoteDetailsAdapter.this.deliveryNoteDetailsList.get(oldItemPosition).getAccountNumber().equals(deliveryNoteDetailsList.get(newItemPosition).getAccountNumber());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return DeliveryNoteDetailsAdapter.this.deliveryNoteDetailsList.get(oldItemPosition).getAccountNumber().equals(deliveryNoteDetailsList.get(newItemPosition).getAccountNumber());
                }
            });
            deliveryNoteDetailsList = _deliveryNoteDetailsList;
            this.deliveryNoteDetailsListFiltered = _deliveryNoteDetailsList;
            result.dispatchUpdatesTo(this);
        }
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCardId;
        private final TextView txtAccountNumber;
        private final TextView txtName;
        private final TextView txtNameAr;
        private final LinearLayout list_delivery_notes_history_row;
        public ViewHolder(View view) {
            super(view);
            list_delivery_notes_history_row = view.findViewById(R.id.list_delivery_notes_history_row);
            txtCardId = view.findViewById(R.id.listViewTxtCardId);
            txtAccountNumber = view.findViewById(R.id.listViewTxtAccountNumber);
            txtName = view.findViewById(R.id.listViewTxtNameEn);
            txtNameAr = view.findViewById(R.id.listViewTxtCardNameAr);
        }
        public TextView getTxtName() {
            return txtName;
        }
        public TextView getTxtNameAr() {
            return txtNameAr;
        }
        public LinearLayout getList_delivery_notes_history_row() {
            return list_delivery_notes_history_row;
        }
        public TextView getTxtCardId() {
            return txtCardId;
        }
        public TextView getTxtAccountNumber() {
            return txtAccountNumber;
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    deliveryNoteDetailsListFiltered = deliveryNoteDetailsList;
                } else {
                    List<DeliveryNoteDetails> filteredList = new ArrayList<>();
                    for (DeliveryNoteDetails movie : deliveryNoteDetailsList) {
                        if (movie.getAccountNumber().toLowerCase().contains(charString.toLowerCase()) || movie.getFullNameAr().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(movie);
                        }
                    }
                    deliveryNoteDetailsListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = deliveryNoteDetailsListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                deliveryNoteDetailsListFiltered = (ArrayList<DeliveryNoteDetails>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }
    public DeliveryNoteDetailsAdapter(List<DeliveryNoteDetails> dataSet, Context context) {
        setDeliveryNotesHistoryList(dataSet);
        setContext(context);
    }
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.delivery_note_details_list_row, viewGroup, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int position) {
        if (position % 2 == 1) {
            viewHolder.getList_delivery_notes_history_row().setBackground(ContextCompat.getDrawable(getContext(), R.drawable.delivery_note_history_table_borders_odd));
        } else {
            viewHolder.getList_delivery_notes_history_row().setBackground(ContextCompat.getDrawable(getContext(), R.drawable.delivery_note_history_table_borders_even));
        }
        viewHolder.getTxtCardId().setText(deliveryNoteDetailsListFiltered.get(position).getCardId().toString());
        viewHolder.getTxtAccountNumber().setText(deliveryNoteDetailsListFiltered.get(position).getAccountNumber());
        viewHolder.getTxtName().setText(deliveryNoteDetailsListFiltered.get(position).getFullNameEn());
        viewHolder.getTxtNameAr().setText(deliveryNoteDetailsListFiltered.get(position).getFullNameAr());
        viewHolder.getTxtNameAr().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        viewHolder.getTxtName().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }
    @Override
    public int getItemCount() {
        if (deliveryNoteDetailsListFiltered != null) {
            return deliveryNoteDetailsListFiltered.size();
        } else {
            return 0;
        }
    }
}