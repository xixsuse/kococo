package co.kr.dwebss.kococo.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import co.kr.dwebss.kococo.R;
import co.kr.dwebss.kococo.adapter.SectionAdapter;
import co.kr.dwebss.kococo.adapter.StatAdapter;
import co.kr.dwebss.kococo.model.Account;
import co.kr.dwebss.kococo.model.RowData;
import co.kr.dwebss.kococo.model.Section;

public class StatFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private Section mAccountsSection;
    private RecyclerView mAccountsRV;
    private PieChart chart;
    List<RowData> accounts;

    public StatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_stat, container, false);

        initializeData();
        mAccountsRV = v.findViewById(R.id.statView);
        //context 를 사용하려면 getActivity를 하면됨
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAccountsRV.setLayoutManager(layoutManager);
//        RecyclerView.Adapter accountsAdapter = new SectionAdapter(mAccountsSection);
        RecyclerView.Adapter accountsAdapter = new StatAdapter(mAccountsSection);
        mAccountsRV.setAdapter(accountsAdapter);
        // Inflate the layout for this fragment

        //파이 차트 시작!

        chart = v.findViewById(R.id.chart1);
        //값을 넣어버리면 퍼센트로바꿈
        chart.setUsePercentValues(false);
        //설명충 등판 (하단에 Description Label이라는게 생김 ㅡㅡ)
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        //폰트체 설정하는부분
//        chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generateCenterSpannableText());
        chart.setCenterTextColor(Color.WHITE);

        //안에 구멍을 넣을지 말지.. 없으면 피자조각처럼 됨
        chart.setDrawHoleEnabled(true);
        //파이 차트 안의 색깔
        chart.setHoleColor(Color.TRANSPARENT);
        //파이 안쪽 투명 테두리 설정 (총수면시간)
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);


        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        //있어야 중간에 텍스트 삽입됨
        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
//        chart.setOnChartValueSelectedListener(this);

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

//        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
        chart.getLegend().setEnabled(false);
        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
//        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);

        //값 넣기
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < accounts.size() ; i++) {
            float val = accounts.get(i).getRowAmount();
            entries.add(new PieEntry(
                    val,
                    accounts.get(i).getRowName()));

            System.out.println("야왜안되 : "+val);
        }

        //라벨이 있을시 목차(legend)의 라벨이 입력됨
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        //파이 사이의 공간 설정
        dataSet.setSliceSpace(3f);

        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        //컬러 설정
        //총 수면시간 (초록) #1EB980 RGB 30, 185, 128
        //코골이 (오렌지) #FF6859 RGB 255, 104, 89
        //이갈이 (옐로우) #FFCF44 RGB 255, 207, 68
        //무호흡 (퍼플) #B15DFF RGB	177, 93, 255

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(30, 185, 128));
        colors.add(Color.rgb(255, 104, 89));
        colors.add(Color.rgb(255, 207, 68));
        colors.add(Color.rgb(177, 93, 255));
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();

        return v;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    //안드로이드에서 TextView에 setText시 text에 부분적으로 밑줄을 긋거나 색상을 바꾸거나 이미지를 중간에 삽입하거나 등이 필요 시
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("평균\n80점");
        //사이즈 크기조절 RelativeSizeSpan
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length()-3, 0);
        s.setSpan(new RelativeSizeSpan(3.7f), s.length()-3, s.length(), 0);


//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        return s;
    }

    //컬러 설정
    //총 수면시간 (초록) #1EB980 RGB 30, 185, 128
    //코골이 (오렌지) #FF6859 RGB 255, 104, 89
    //이갈이 (옐로우) #FFCF44 RGB 255, 207, 68
    //무호흡 (퍼플) #B15DFF RGB	177, 93, 255
    private void initializeData() {
        accounts = new ArrayList<RowData>();
        accounts.add(new Account("총 수면시간","8시간", 480f, "1", 0xFF1EB980));
        accounts.add(new Account("코골이", "1시간 20분",80f, "1", 0xFFFF6859));
        accounts.add(new Account("무호흡","20분", 20f, "1", 0xFFFFCF44));
        accounts.add(new Account("이갈이","20분", 20f, "1", 0xFFB15DFF));
        mAccountsSection = new Section(accounts, "수면 점수", false);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


}
