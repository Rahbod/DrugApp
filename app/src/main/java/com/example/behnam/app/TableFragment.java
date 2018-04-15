package com.example.behnam.app;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;



public class TableFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_web_view, container);
        String tableHtml = getArguments().getString("table");
        String styleTable = tableHtml.replaceAll("<table>", "<table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" dir=\"rtl\">");
        String styleTd = styleTable.replaceAll("<td><span>", "<td style='width:86px'><span style='color:#EE82EE'><span dir=\"LTR\">");
        WebView webView = view.findViewById(R.id.webView);
        webView.loadData("<html dir = 'rtl'><body>" + styleTd + "</body></html>", "text/html ; charset=utf-8", "utf-8");

        return view;
    }
}
