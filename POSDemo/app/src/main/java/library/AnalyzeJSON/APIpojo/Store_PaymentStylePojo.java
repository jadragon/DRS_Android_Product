package library.AnalyzeJSON.APIpojo;

import java.util.ArrayList;

public class Store_PaymentStylePojo {
    private StoreStypePojoItem store;
    private PaymentStypePojoItem payment;

    public StoreStypePojoItem getStore() {
        return store;
    }

    public void setStore(StoreStypePojoItem store) {
        this.store = store;
    }

    public PaymentStypePojoItem getPayment() {
        return payment;
    }

    public void setPayment(PaymentStypePojoItem payment) {
        this.payment = payment;
    }

    public class StoreStypePojoItem {
        ArrayList<String> s_no;
        ArrayList<String> store;

        public StoreStypePojoItem(ArrayList<String> s_no, ArrayList<String> store) {
            this.s_no = s_no;
            this.store = store;
        }

        public ArrayList<String> getS_no() {
            return s_no;
        }

        public ArrayList<String> getStore() {
            return store;
        }
    }

    public class PaymentStypePojoItem {
        ArrayList<String> p_no;
        ArrayList<String> style;

        public PaymentStypePojoItem(ArrayList<String> p_no, ArrayList<String> style) {
            this.p_no = p_no;
            this.style = style;
        }

        public ArrayList<String> getP_no() {
            return p_no;
        }

        public ArrayList<String> getStyle() {
            return style;
        }
    }
}
