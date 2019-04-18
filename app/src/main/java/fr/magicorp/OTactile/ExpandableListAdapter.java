package fr.magicorp.OTactile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<Order> listDataOrder;
    private HashMap<Order,List<OrderProduct>> listHashOrderProducts;

    public ExpandableListAdapter(Context mContext, List<Order> listDataOrder, HashMap<Order, List<OrderProduct>> listHashOrderProducts) {
        this.mContext = mContext;
        this.listDataOrder = listDataOrder;
        this.listHashOrderProducts = listHashOrderProducts;
    }

    @Override
    public int getGroupCount() {
        return listDataOrder.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashOrderProducts.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataOrder.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashOrderProducts.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Order order = (Order)getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.order_list_item, null);
        }

        TextView no = (TextView) convertView.findViewById(R.id.noOrder);
        TextView total = (TextView) convertView.findViewById(R.id.totalOrder);
        TextView shipping = (TextView) convertView.findViewById(R.id.shippingOrder);
        TextView deliveryAddress = (TextView) convertView.findViewById(R.id.deliveryAddressOrder);

        no.setText(String.valueOf(order.getId()));
        total.setText(String.valueOf(order.getTotal()));
        shipping.setText(String.valueOf(order.getShipping()));
        deliveryAddress.setText(String.valueOf(order.getAddress()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final OrderProduct orderProduct = (OrderProduct) getChild(groupPosition,childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.order_list_content_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.titleOrderedProduct);
        TextView reference = (TextView) convertView.findViewById(R.id.referenceOrderedProduct);
        TextView price = (TextView) convertView.findViewById(R.id.priceOrderedProduct);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantityOrderedProduct);

        title.setText(String.valueOf(orderProduct.getTitle()));
        reference.setText(String.valueOf(orderProduct.getReference()));
        price.setText(NumberFormat.getInstance(Locale.getDefault()).format(orderProduct.getPriceTTC())+"€");
        quantity.setText(String.valueOf(orderProduct.getQuantity()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
