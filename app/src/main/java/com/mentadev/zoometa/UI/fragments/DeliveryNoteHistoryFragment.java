package com.mentadev.zoometa.UI.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textview.MaterialTextView;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.activities.LandingActivity;
import com.mentadev.zoometa.UI.activities.LoginActivity;
import com.mentadev.zoometa.UI.listadapters.DeliveryNoteAdapter;
import com.mentadev.zoometa.datamodel.DeliveryNoteScanning;
import com.mentadev.zoometa.exceptionhandling.ExceptionHandling;
import com.mentadev.zoometa.restconnector.FinderREST;
import com.mentadev.zoometa.restconnector.MyProfileInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class DeliveryNoteHistoryFragment extends BaseFragment {


    DatePickerDialog picker;
    SwipeRefreshLayout swipeContainer;
    EditText dateFromDeliveryNoteHistory;
    EditText dateToDeliveryNoteHistory;
    MaterialTextView textinput_error;
    LinearLayout header_group;
    LinearLayout errorTextGroup;
    ProgressBar simpleProgressBar;
    LinearLayoutCompat tableContainer;
    DeliveryNoteAdapter deliveryNoteAdapter;
    RecyclerView recyclerView;
    MaterialTextView txt_empty_delivery_notes_history;
    List<DeliveryNoteScanning> deliveryNoteScannings;
    RecyclerView.AdapterDataObserver adapterDataObserver;
    private Date getDateFromText(EditText txtDate){
        List<String> date = Arrays.asList(txtDate.getText().toString().split("/"));
        int year = Integer.parseInt(date.get(2));
        int month = Integer.parseInt(date.get(0));
        int day = Integer.parseInt(date.get(1));
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day,0,0);
        return c.getTime();
    }
    public static DeliveryNoteHistoryFragment newInstance() {
        return new DeliveryNoteHistoryFragment();
    }
    private void setDateFromText(EditText txtDate, Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        txtDate.setText((month + 1) + "/" + day + "/" + year);
    }
    private Date getDateFrom(){
        return getDateFromText(dateFromDeliveryNoteHistory);
    }
    private Date getDateTo(){
        return getDateFromText(dateToDeliveryNoteHistory);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_note_history_fragment, container, false);
        recyclerView = view.findViewById(R.id.list_delivery_notes_history);
        textinput_error = view.findViewById(R.id.textinput_error);
        txt_empty_delivery_notes_history = view.findViewById(R.id.txt_empty_delivery_notes_history);
        simpleProgressBar = view.findViewById(R.id.simpleProgressBar);
        textinput_error.setVisibility(View.INVISIBLE);
        dateFromDeliveryNoteHistory = view.findViewById(R.id.dateFromDeliveryNoteHistory);
        tableContainer = view.findViewById(R.id.tableContainer);
        header_group = view.findViewById(R.id.header_group);
        errorTextGroup = view.findViewById(R.id.errorTextGroup);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> getMyScannedDeliveryNotes(true));
        dateToDeliveryNoteHistory = view.findViewById(R.id.dateToDeliveryNoteHistory);

        resetDates();
        dateFromDeliveryNoteHistory.setOnClickListener((View v) -> {

            List<String> date = Arrays.asList(dateFromDeliveryNoteHistory.getText().toString().split("/"));
            int year = Integer.parseInt(date.get(2));
            int month = Integer.parseInt(date.get(0));
            int day = Integer.parseInt(date.get(1));
            picker = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                            dateFromDeliveryNoteHistory.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            getMyScannedDeliveryNotes(false);
                        }
                    }, year, month - 1, day);
            picker.show();
        });
        dateToDeliveryNoteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> date = Arrays.asList(dateToDeliveryNoteHistory.getText().toString().split("/"));
                int year = Integer.parseInt(date.get(2));
                int month = Integer.parseInt(date.get(0));
                int day = Integer.parseInt(date.get(1));
                picker = new DatePickerDialog(getActivity(),
                        (DatePicker view12, int year1, int monthOfYear, int dayOfMonth) -> {
                            dateToDeliveryNoteHistory.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year1);
                            getMyScannedDeliveryNotes(false);
                        }, year, month - 1, day);
                picker.show();
            }
        });
        deliveryNoteScannings = new ArrayList<>();
        deliveryNoteAdapter = new DeliveryNoteAdapter(deliveryNoteScannings, getContext());
        recyclerView.setAdapter(deliveryNoteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getMyScannedDeliveryNotes(false);
        return view;
    }

    private void resetDates() {
        setDateFromText(dateToDeliveryNoteHistory, Calendar.getInstance());
        final Calendar cldr = Calendar.getInstance();
        cldr.add(Calendar.MONTH, -1);
        setDateFromText(dateFromDeliveryNoteHistory, cldr);
    }


    private void getMyScannedDeliveryNotes(boolean isRefresh) {
        errorTextGroup.setVisibility(View.GONE);
        simpleProgressBar.setVisibility(!isRefresh ? View.VISIBLE : View.GONE);
        tableContainer.setVisibility(View.GONE);
        if (getDateFrom().compareTo(getDateTo()) > 0) {
            Toast.makeText(getActivity(), getResources().getText(R.string.errorText_FromDateLessThanToDate), Toast.LENGTH_LONG).show();
            resetDates();
            getMyScannedDeliveryNotes(isRefresh);
        }
        FinderREST controller = new FinderREST(getActivity(), LandingActivity.MyProfile.getToken());
        controller.getScanningHistory(String.valueOf(dateFromDeliveryNoteHistory.getText()),
                String.valueOf(dateToDeliveryNoteHistory.getText()), new MyProfileInterface.ScanningHistoryInterface() {
                    @Override
                    public void getScanningHistory(List<DeliveryNoteScanning> deliveryNoteScannings) {
                        if (!deliveryNoteScannings.isEmpty()) {

                            tableContainer.setVisibility(View.VISIBLE);
                            txt_empty_delivery_notes_history.setVisibility(View.GONE);
                            errorTextGroup.setVisibility(View.GONE);

                        } else {
                            tableContainer.setVisibility(View.GONE);
                            errorTextGroup.setVisibility(View.VISIBLE);
                            txt_empty_delivery_notes_history.setVisibility(View.VISIBLE);
                        }

                        textinput_error.setVisibility(View.GONE);
                        deliveryNoteAdapter.setDeliveryNotesHistoryList(deliveryNoteScannings);
                        deliveryNoteAdapter.notifyDataSetChanged();
                        simpleProgressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void OnError(Throwable exception) {
                        errorTextGroup.setVisibility(View.VISIBLE);
                        ExceptionHandling.HandleUIDataEntryValidation(getActivity(), new Exception(R.string.message_error_get_delivery_note_history + " " + exception.getMessage()),
                                textinput_error);
                        simpleProgressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void OnUnauthorized() {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    }

                    @Override
                    public void OnResourceNotFound() {
                        OnError(new Throwable(getContext().getResources().getString(R.string.message_error_resource_not_found)));
                    }

                    @Override
                    public void OnInternalServerError() {
                        OnError(new Throwable(getContext().getResources().getString(R.string.message_error_internal_server_error)));

                    }

                    @Override
                    public void OnGenericError() {
                        OnError(new Throwable(getContext().getResources().getString(R.string.message_error_unknown_error)));
                    }
                });

    }
}