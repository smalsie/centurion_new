package com.joshuahugh.cent2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joshuahugh on 06/03/15.
 */
public class MissedShotsFragment extends AbsFrag {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.toilet, container, false);

        DBHelper dH = new DBHelper(rootView.getContext());
        SQLiteDatabase db = dH.getReadableDatabase();

        ArrayList<ToiletItem> homeItems = dH.getToilets(db);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview);

        final ContactArrayAdaptor arrayAdapter = new ContactArrayAdaptor(getActivity(), homeItems);

        Button addNameButton = (Button) rootView.findViewById(R.id.addNameButton);

        addNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("jknjk", "nklnkkn2)");
                Intent intent = new Intent(rootView.getContext(), AddName.class);
                startActivity(intent);
            }
        });

        Button refreshButton = (Button) rootView.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.clear();
                DBHelper dH = new DBHelper(rootView.getContext());
                SQLiteDatabase db = dH.getReadableDatabase();
                ArrayList<ToiletItem> homeItems = dH.getToilets(db);
                arrayAdapter.addAll(homeItems);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        listView.setAdapter(arrayAdapter);

        return rootView;

    }

    @Override
    public String getName() {
        return "Toilets";
    }

    private class ContactArrayAdaptor extends ArrayAdapter<ToiletItem> {
        private final Context context;
        private final ArrayList<ToiletItem> homeItems;

        public ContactArrayAdaptor(Context context,ArrayList<ToiletItem> homeItems) {

            super(context, R.layout.home_fragment,homeItems);
            this.context = context;
            this.homeItems = homeItems;
        }

        /**
         * Overide the view method
         *
         * @return a view (the list of all of the events)
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //get the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate events_list.xml
            final View rowView = inflater.inflate(R.layout.toilet_item, parent, false);

            //Each of the textviews to add specified content to
            TextView name= (TextView) rowView.findViewById(R.id.name);
            TextView shots = (TextView) rowView.findViewById(R.id.shotsMissed);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(rowView.getContext(), homeItems.get(position).getId());
                }
            });


            name.setText(homeItems.get(position).getName());
            shots.setText(homeItems.get(position).getShots() + " ");

            return rowView;
        }

        private void delete(Context c, final int id) {
            final Context cc = c;
            new AlertDialog.Builder(c)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure you want to quit?")
                    .setMessage("Quit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DBHelper dH = new DBHelper(context);
                            SQLiteDatabase db = dH.getReadableDatabase();
                            dH.remove(db, id);



                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
