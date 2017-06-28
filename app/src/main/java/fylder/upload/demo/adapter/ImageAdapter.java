package fylder.upload.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fylder.upload.demo.R;
import fylder.upload.demo.model.ImageModel;

/**
 * Created by fylder on 2017/6/28.
 */

public class ImageAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ImageModel> datas = new ArrayList<>();

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<ImageModel> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_lay, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageModel data = datas.get(position);
        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        String index = position + 1 + "";
        viewHolder.indexText.setText(index);
        viewHolder.progressBar.setProgress(data.getPro());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image_index_text)
        TextView indexText;
        @BindView(R.id.item_image_img)
        ImageView imageView;
        @BindView(R.id.item_image_pro)
        ProgressBar progressBar;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
