package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.CouponAdapter;
import com.example.alex.posdemo.adapter.recylclerview.ProductListAdapter;
import com.example.alex.posdemo.adapter.recylclerview.QuickMenuAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.Store_PaymentStylePojo;
import library.AnalyzeJSON.AnalyzeUtil;
import library.AnalyzeJSON.Analyze_CountInfo;
import library.Component.ToastMessageDialog;
import library.JsonApi.CountApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_count extends Fragment {
    View v;
    UserInfo userInfo;
    Spinner count_store, count_payment, count_spinner_returntype;
    EditText count_edit_en, count_edit_m_type, count_edit_member_order;
    TextView count_txt_en, count_txt_m_type, count_edit_pcode;
    RecyclerView count_productlist_recyclerview, count_coupon_recyclerview,count_quickmenu_recylcerview;
    ProductListAdapter productListAdapter;
    QuickMenuAdapter quickMenuAdapter;
    CouponAdapter couponAdapter;
    Switch count_switch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_count_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();
        initQuickMenu();
        initSearchEnAndSearchmember();
        initSearch_Pcode();
        initSearch_Member_Order();
        initProductListRecyclerView(null);
        initCouponRecyclerView(null);
        initSwitch();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new CountApi().store_payment_style(userInfo.getS_no());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initStoreAndPayment(jsonObject);
            }
        });

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new CountApi().preferential_content(0);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initCouponRecyclerView(jsonObject);
            }
        });
        return v;
    }

    private void initQuickMenu() {
        count_quickmenu_recylcerview = v.findViewById(R.id.count_quickmenu_recylcerview);
        Map<String, Integer> map = new HashMap<>();
        map.put("image", R.array.quick_menu_image);
        map.put("text", R.array.quick_menu_txt);
        quickMenuAdapter = new QuickMenuAdapter(getContext(), map);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        count_quickmenu_recylcerview.setLayoutManager(layoutManager);
        count_quickmenu_recylcerview.setAdapter(quickMenuAdapter);
    }


    private void initSwitch() {
        count_switch = v.findViewById(R.id.count_switch);
        count_spinner_returntype.setEnabled(false);
        count_edit_member_order.setEnabled(false);
        count_spinner_returntype.setBackgroundColor(getResources().getColor(R.color.punch_gray1));
        count_edit_member_order.setBackgroundColor(getResources().getColor(R.color.punch_gray1));
        count_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initProductListRecyclerView(null);
                if (isChecked) {
                    productListAdapter.setType(ProductListAdapter.TYPE_RETURN);
                    count_spinner_returntype.setEnabled(true);
                    count_edit_member_order.setEnabled(true);
                    count_spinner_returntype.setBackgroundColor(getResources().getColor(R.color.white));
                    count_edit_member_order.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    productListAdapter.setType(ProductListAdapter.TYPE_SELL);
                    count_spinner_returntype.setEnabled(false);
                    count_edit_member_order.setEnabled(false);
                    count_spinner_returntype.setBackgroundColor(getResources().getColor(R.color.punch_gray1));
                    count_edit_member_order.setBackgroundColor(getResources().getColor(R.color.punch_gray1));
                }
            }
        });
    }

    private void initSearch_Pcode() {
        count_edit_pcode = v.findViewById(R.id.count_edit_pcode);
        count_edit_pcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public void onTaskBefore() {

                        }

                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new CountApi().product_item(count_edit_pcode.getText().toString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                productListAdapter.addItem(jsonObject);
                            } else {
                                new ToastMessageDialog(getContext(), ToastMessageDialog.TYPE_ERROR).confirm(AnalyzeUtil.getMessage(jsonObject));
                            }
                        }
                    });
                }
            }
        });


    }

    private void initProductListRecyclerView(JSONObject jsonObject) {
        if (productListAdapter != null && count_productlist_recyclerview != null) {
            productListAdapter.setFilter(jsonObject);
        } else {
            count_productlist_recyclerview = v.findViewById(R.id.count_productlist_recyclerview);
            productListAdapter = new ProductListAdapter(getContext(), jsonObject, ProductListAdapter.TYPE_SELL);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            count_productlist_recyclerview.setLayoutManager(layoutManager);
            count_productlist_recyclerview.setAdapter(productListAdapter);
        }

    }

    private void initCouponRecyclerView(JSONObject jsonObject) {
        if (couponAdapter != null && count_coupon_recyclerview != null) {
            couponAdapter.setFilter(jsonObject);
        } else {
            count_coupon_recyclerview = v.findViewById(R.id.count_coupon_recyclerview);
            couponAdapter = new CouponAdapter(getContext(), jsonObject);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            count_coupon_recyclerview.setLayoutManager(layoutManager);
            count_coupon_recyclerview.setAdapter(couponAdapter);
        }

    }

    private void initSearch_Member_Order() {
        count_spinner_returntype = v.findViewById(R.id.count_spinner_returntype);
        count_edit_member_order = v.findViewById(R.id.count_edit_member_order);
        count_edit_member_order.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!count_edit_member_order.getText().toString().equals("")) {
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public void onTaskBefore() {

                            }

                            @Override
                            public JSONObject onTasking(Void... params) {
                                return new CountApi().search_member_order(count_edit_member_order.getText().toString());
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                initProductListRecyclerView(jsonObject);
                            }
                        });
                    } else {
                        initProductListRecyclerView(null);
                    }
                }
            }
        });
    }

    private void initSearchEnAndSearchmember() {
        //employee
        count_edit_en = v.findViewById(R.id.count_edit_en);
        count_txt_en = v.findViewById(R.id.count_txt_en);
        count_edit_en.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!count_edit_en.getText().toString().equals("")) {
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public void onTaskBefore() {

                            }

                            @Override
                            public JSONObject onTasking(Void... params) {
                                return new CountApi().employee_name(count_edit_en.getText().toString());
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                try {
                                    count_txt_en.setText(jsonObject.getJSONObject("Data").getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {
                                        new ToastMessageDialog(getContext(), ToastMessageDialog.TYPE_ERROR).confirm(jsonObject.getString("Message"));
                                        count_edit_en.requestFocus();
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        new ToastMessageDialog(getContext(), ToastMessageDialog.TYPE_ERROR).confirm("伺服器異常");
                                    }
                                }
                            }
                        });
                    } else {
                        count_txt_en.setText("");
                    }
                }
            }
        });
//member
        count_edit_m_type = v.findViewById(R.id.count_edit_m_type);
        count_txt_m_type = v.findViewById(R.id.count_txt_m_type);
        count_edit_m_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!count_edit_m_type.getText().toString().equals("")) {
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public void onTaskBefore() {

                            }

                            @Override
                            public JSONObject onTasking(Void... params) {
                                return new CountApi().member_data(count_edit_m_type.getText().toString());
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                try {
                                    count_txt_m_type.setText(jsonObject.getJSONObject("Data").getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {
                                        new ToastMessageDialog(getContext(), ToastMessageDialog.TYPE_ERROR).confirm(jsonObject.getString("Message"));
                                        count_edit_m_type.requestFocus();
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        new ToastMessageDialog(getContext(), ToastMessageDialog.TYPE_ERROR).confirm("伺服器異常");
                                    }
                                }
                            }
                        });
                    } else {
                        count_txt_m_type.setText("");
                    }
                }
            }
        });
    }

    private void initStoreAndPayment(JSONObject jsonObject) {
        count_store = v.findViewById(R.id.count_store);
        count_payment = v.findViewById(R.id.count_payment);
        //store
        Store_PaymentStylePojo store_paymentStylePojo = new Analyze_CountInfo().getStore_Payment_Style(jsonObject);
        count_store.setAdapter(new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, store_paymentStylePojo.getStore().getStore()));
        //payment
        count_payment.setAdapter(new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, store_paymentStylePojo.getPayment().getStyle()));
    }


}
