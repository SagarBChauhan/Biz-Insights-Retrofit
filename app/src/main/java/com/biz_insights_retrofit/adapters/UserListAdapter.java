package com.biz_insights_retrofit.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.biz_insights_retrofit.R;
import com.biz_insights_retrofit.models.UserDataModel;
import com.biz_insights_retrofit.utility.Globals;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    ArrayList<UserDataModel.Rows> userListData;
    Globals globals;
    Context context;

    public UserListAdapter(Context context, ArrayList<UserDataModel.Rows> userListData) {
        this.context = context;
        globals = (Globals) context.getApplicationContext();
        this.userListData = userListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (globals.getGridValue() == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_grid, parent, false);
        } else if (globals.getGridValue() == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDataModel.Rows users = userListData.get(position);
        holder.text_user_name.setText(users.first_name + " " + users.last_name);
        holder.text_user_email.setText(users.email_id);
        holder.text_user_mobile_no.setText(users.mobile_no);
        holder.text_user_address.setText(users.address);
    }

    @Override
    public int getItemCount() {
        return userListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView text_user_name, text_user_email, text_user_mobile_no, text_user_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.text_user_name = itemView.findViewById(R.id.text_user_name);
            this.text_user_email = itemView.findViewById(R.id.text_user_email);
            this.text_user_mobile_no = itemView.findViewById(R.id.text_user_mobile_no);
            this.text_user_address = itemView.findViewById(R.id.text_user_address);
        }
    }
}
