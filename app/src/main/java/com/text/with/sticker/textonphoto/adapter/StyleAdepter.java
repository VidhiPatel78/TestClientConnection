package com.text.with.sticker.textonphoto.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.text.with.sticker.textonphoto.R;
import com.text.with.sticker.textonphoto.interfacelistner.GetTemplateListener;


public class StyleAdepter extends RecyclerView.Adapter<RecyclerViewHolder> {
    View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            StyleAdepter styleAdepter = StyleAdepter.this;
            styleAdepter.onGetTemplate = (GetTemplateListener) styleAdepter.context;
            int position = ((RecyclerViewHolder) v.getTag()).getPosition();
            if (StyleAdepter.this.val.equals("template")) {
                StyleAdepter.this.onGetTemplate.ontemplate(position + 1);
            }
        }
    };
    Activity context;
    LayoutInflater inflater;
    GetTemplateListener onGetTemplate;
    String[] templateImageid = {"at1", "at2", "at3", "at4", "at5", "at6", "at7", "at8", "at9", "at10", "at11", "at12", "at13", "at14", "at15", "at16", "at17", "at18", "at19", "at20", "at21", "at22", "at23", "at24", "at25", "at26"};
    String val;

    public StyleAdepter(Activity context2, String val2) {
        this.context = context2;
        this.val = val2;
        this.inflater = LayoutInflater.from(context2);
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(this.inflater.inflate(R.layout.item_template, parent, false));
    }

    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (this.val.equals("template")) {
            holder.imageView.setImageResource(this.context.getResources().getIdentifier(this.templateImageid[position], "drawable", this.context.getPackageName()));
        }
        holder.imageView.setOnClickListener(this.clickListener);
        holder.imageView.setTag(holder);
    }

    public int getItemCount() {
        if (this.val.equals("template")) {
            return this.templateImageid.length;
        }
        return 0;
    }
}
