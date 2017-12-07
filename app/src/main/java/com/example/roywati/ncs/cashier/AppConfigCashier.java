package com.example.roywati.ncs.cashier;


public class AppConfigCashier {

    public static String get_unpaid_orders="unpaid-orders1.php";
    public static String get_paid_orders="paid-orders.php";
    public static String make_payment="make-payment.php";

    public static String print_receipt="receipt-details.php";
    public static String print_bill_receipt="receipt-bill-details.php";
    public static String print_order_receipt="receipt-order-details.php";
    public static String close_orders="close-orders.php";
    public static String generate_bill="generate-bill.php";




    public static String userId="";
    public static String homepageId="";

    public static String orderNumber="";
    public static String amount="";
    public static String amountGivenToCashier="";
    public static String total="";

    public static String[] order_item_id;
    public static String[] order_id;
    public static String[] order_id_generate_bill;
    public static String[] price;

    public static int changeAmount=0;





}
