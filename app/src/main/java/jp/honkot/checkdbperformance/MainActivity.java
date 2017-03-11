package jp.honkot.checkdbperformance;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import jp.honkot.checkdbperformance.databinding.ActivityMainBinding;
import jp.honkot.checkdbperformance.orma.OrmaDao;
import jp.honkot.checkdbperformance.pure.PureAndroidDao;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    ActivityMainBinding mBinding;

    OrmaDao ormaDao;

    PureAndroidDao pureAndroidDao;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setHandler(this);

        initialize();
    }

    private void initialize() {
        ormaDao = new OrmaDao(getApplicationContext());
        ormaDao.initProduct();

        pureAndroidDao = new PureAndroidDao(getApplicationContext());
        pureAndroidDao.initProduct();

        updateEventCount();
    }

    @Override
    public void onClick(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        BgExecuter executer = new BgExecuter(view.getId());
        executer.execute(new String[] {"a"});
    }

    private class BgExecuter extends AsyncTask<String, IAction, Performance> {

        int viewId;
        IAction controller;
        String tag;
        Performance bgPerformance = new Performance();

        BgExecuter(int viewId) {
            this.viewId = viewId;
        }

        @Override
        protected void onPreExecute() {
            bgPerformance.measureStart();
            if (mBinding.radioOrma.isChecked()) {
                controller = ormaDao;
                tag = "Orma ";

            } else if (mBinding.radioPureAndroid.isChecked()) {
                controller = pureAndroidDao;
                tag = "Pure Android ";

            } else if (mBinding.radioRealm.isChecked()) {
                //TODO
            }

            super.onPreExecute();
        }

        @Override
        protected Performance doInBackground(String... args) {
            if (controller != null) {
                switch (viewId) {
                    case R.id.buttonDeleteAll:
                        tag += "delete all event";
                        return controller.initialize();

                    case R.id.buttonInsert10000_one:
                        tag += "insert 10000 one by one ";
                        return controller.insert();

                    case R.id.buttonInsert10000_bulk:
                        tag += "insert 10000 bulk ";
                        return controller.insertBulk();

                    case R.id.buttonUpdate:
                        tag += "update related Product at random ";
                        return controller.update();

                    case R.id.buttonDelete:
                        tag += "delete 10000 one by one ";
                        return controller.delete();

                    case R.id.buttonSumQty1_1:
                        tag += "sum Qty in Event with Product '1' / SIMPLE WAY ";
                        return controller.sumQtyBySimple();

                    case R.id.buttonSumQty1_2:
                        tag += "sum Qty in Event with Product '1' / FASTER WAY ";
                        return controller.sumQtyByFaster();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Performance result) {
            bgPerformance.measureFinish();
            if (result != null) {
                mBinding.result.setText(
                        result.getDisplayResult(tag) + "\n" +
                        mBinding.result.getText());
                int percent = (int)(((double)result.getTotalScore() / (double)bgPerformance.getTotalScore()) * 100);
                Log.d(getClass().getSimpleName(), "### " + result.getDisplayResult(tag)
                        + "\n" + "Score: " + result.getTotalScore() + "ms (" + percent + "%) / " + bgPerformance.getTotalScore() + "ms");
            } else {
                mBinding.result.setText(
                        "Error occurred." + "\n" +
                        mBinding.result.getText());
            }
            updateEventCount();
            progressDialog.dismiss();
        }
    }

    private void updateEventCount() {
        mBinding.radioPureAndroid.setText(
                "Pure Android (" + pureAndroidDao.allEventCount() + " records)");

        mBinding.radioOrma.setText(
                "Orma (" + ormaDao.allEventCount() + " records)");

    }
}
