package com.mentadev.zoometa.UI.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textview.MaterialTextView;
import com.mentadev.zoometa.R;
import com.mentadev.zoometa.UI.listadapters.DeliveryNoteDetailsAdapter;
import com.mentadev.zoometa.datamodel.DeliveryNoteDetails;
import com.mentadev.zoometa.exceptionhandling.ExceptionHandling;
import com.mentadev.zoometa.restconnector.FinderREST;
import com.mentadev.zoometa.restconnector.MyProfileInterface;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteDetailsActivity extends AppCompatActivity {

    private List<DeliveryNoteDetails> deliveryNoteScannings;
    private DeliveryNoteDetailsAdapter deliveryNoteAdapter;
    RecyclerView recyclerView;
    LinearLayout errorTextGroup;
    ProgressBar simpleProgressBar;
    LinearLayoutCompat tableContainer;
    MaterialTextView txt_empty_delivery_notes_history;
    MaterialTextView textinput_error;
    SwipeRefreshLayout swipeContainer;
    MaterialTextView txt_delivery_note_name;
    LinearLayout container_header_name;
    LinearLayout container_header_count;
    MaterialTextView txt_cards_count;
    private SearchView searchView;
    RecyclerView.AdapterDataObserver adapterDataObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_note_details_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getResources().getString(R.string.delivery_note_details_activity_header));
        recyclerView = findViewById(R.id.list_delivery_notes_history);
        textinput_error = findViewById(R.id.textinput_error);
        txt_empty_delivery_notes_history = findViewById(R.id.txt_empty_delivery_notes_history);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        textinput_error.setVisibility(View.INVISIBLE);
        tableContainer = findViewById(R.id.tableContainer);
        errorTextGroup = findViewById(R.id.errorTextGroup);
        txt_cards_count = findViewById(R.id.txt_cards_count_display);
        swipeContainer = findViewById(R.id.swipeContainer);
        container_header_count = findViewById(R.id.container_header_count);
        container_header_name = findViewById(R.id.container_header_name);

        txt_delivery_note_name = findViewById(R.id.txt_delivery_note_name);
        swipeContainer.setOnRefreshListener(() ->
                {
                    searchView.setQuery("", false);
                    bindDeliveryNoteDetails(true);
                }
        );
        deliveryNoteId = getIntent().getStringExtra("ID");
        String deliveryNoteName = getIntent().getStringExtra("NAME");
        deliveryNoteScannings = new ArrayList<>();
        deliveryNoteAdapter = new DeliveryNoteDetailsAdapter(deliveryNoteScannings, getApplicationContext());
        recyclerView.setAdapter(deliveryNoteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        txt_delivery_note_name.setText(deliveryNoteName);
        adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                txt_cards_count.setText(String.valueOf(deliveryNoteAdapter.getItemCount()));
            }
        };
        deliveryNoteAdapter.registerAdapterDataObserver(adapterDataObserver);
        bindDeliveryNoteDetails(false);
    }


    String deliveryNoteId = "";

    private void bindDeliveryNoteDetails(boolean isRefresh) {
        errorTextGroup.setVisibility(View.GONE);
        simpleProgressBar.setVisibility(!isRefresh ? View.VISIBLE : View.GONE);
        tableContainer.setVisibility(View.GONE);
        container_header_name.setVisibility(isRefresh ? View.VISIBLE : View.GONE);
        container_header_count.setVisibility(isRefresh ? View.VISIBLE : View.GONE);
        FinderREST controller = new FinderREST(getApplication(), LandingActivity.MyProfile.getToken());

        controller.getDeliverNoteDetails(deliveryNoteId, new MyProfileInterface.DeliverNoteDetailsInterface() {
            @Override
            public void getDeliverNoteDetailsInterface(List<DeliveryNoteDetails> deliveryNoteDetails) {
                deliveryNoteScannings = deliveryNoteDetails;
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
                txt_cards_count.setText(String.valueOf(deliveryNoteAdapter.getItemCount()));
                simpleProgressBar.setVisibility(View.GONE);
                container_header_count.setVisibility(View.VISIBLE);
                container_header_name.setVisibility(View.VISIBLE);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void OnError(Throwable exception) {
                errorTextGroup.setVisibility(View.VISIBLE);
                ExceptionHandling.HandleUIDataEntryValidation(getApplication(), new Exception(getResources().getString(R.string.message_error_get_delivery_note_history) + " " + exception.getMessage()),
                        textinput_error);
                simpleProgressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void OnUnauthorized() {
                LoginActivity.LoginToZoomenta(getApplicationContext(), getIntent());
//                Intent intent = new Intent(getApplication(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplication().startActivity(intent);
            }

            @Override
            public void OnResourceNotFound() {
                OnError(new Throwable(getApplication().getResources().getString(R.string.message_error_resource_not_found)));
            }

            @Override
            public void OnInternalServerError() {
                OnError(new Throwable(getApplication().getResources().getString(R.string.message_error_internal_server_error)));

            }

            @Override
            public void OnGenericError() {
                OnError(new Throwable(getApplication().getResources().getString(R.string.message_error_unknown_error)));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                deliveryNoteAdapter.getFilter().filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                deliveryNoteAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}