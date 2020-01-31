package devcom.marcos.triangularpos;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import devcom.marcos.triangularpos.trilateration.Point;
import devcom.marcos.triangularpos.trilateration.Trilateration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.accent_systems.ibks_sdk.scanner.ASBleScanner;
import com.accent_systems.ibks_sdk.scanner.ASScannerCallback;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import static devcom.marcos.triangularpos.trilateration.Trilateration.Compute;

public class MainActivity extends AppCompatActivity {

    final int K = -63;
    Context here;
    MyView v;
    Paint pBeacon;
    Paint pSol;
    Paint pText;
    Paint pRange;
    ASBleScanner scanner;
    double[] bRssi = new double[3];
    double[] centroid;
    final Beacon b1 = new Beacon("C9:18:D4:BD:25:D8","iSBK105 #1");
    final Beacon b2 = new Beacon("F3:CF:9B:BA:B3:2C","iSBK105 #2");
    final Beacon b3 = new Beacon("EB:FA:E4:E1:CB:2E","iSBK105 #3");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Aseguramos Permisos
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 0);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 0);
        }
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

        //Beacon conf
        b1.setLatLon(100, 100);
        b2.setLatLon(200, 200);
        b3.setLatLon(300, 100);

        //Paints para los puntos en pantalla
        pBeacon = new Paint();
        pBeacon.setStyle(Paint.Style.FILL);
        pBeacon.setColor(Color.BLUE);

        pRange = new Paint();
        pRange.setStyle(Paint.Style.FILL);
        pRange.setStrokeWidth(10);
        pRange.setColor(Color.RED);

        pSol = new Paint();
        pSol.setStyle(Paint.Style.FILL);
        pSol.setStrokeWidth(35);
        pSol.setColor(Color.GREEN);

        pText = new Paint();
        pText.setTextSize(25);
        pText.setColor(Color.GRAY);

        //Inicio Programa
        super.onCreate(savedInstanceState);
        here = this;
        //v = new MyView(this);
        //setContentView(v);

        ASBleScanner scanner = new ASBleScanner((Activity) here, new ASScannerCallback() {
            @Override
            public void scannedBleDevices(ScanResult result) {
                //iSBK105 #1 --> C9:18:D4:BD:25:D8
                //iSBK105 #2 --> F3:CF:9B:BA:B3:2C
                //iSBK105 #3 --> EB:FA:E4:E1:CB:2E
                double rssi, dis;
                rssi = result.getRssi();
                dis = calculateDistance(rssi);


                switch(result.getDevice().getAddress()){
                    case "C9:18:D4:BD:25:D8":
                        b1.setDist((float)dis);
                        Log.i("beacon1",rssi+" "+(float)dis);
                        break;
                    case "F3:CF:9B:BA:B3:2C":
                        b2.setDist((float)dis);
                        Log.i("beacon2",rssi+" "+(float)dis);
                        break;
                    case "EB:FA:E4:E1:CB:2E":
                        b3.setDist((float)dis);
                        Log.i("beacon3",rssi+" "+(float)dis);
                        break;
                }

                if(b1.getDist() != 0.0f && b2.getDist() != 0.0f && b3.getDist() != 0.0f) {
                    double[][] positions = new double[][]{{b1.getLat(), b1.getLon()}, {b2.getLat(), b2.getLon()},
                            {b3.getLat(), b3.getLon()}};
                    double[] distances = new double[]{b1.getDist(), b2.getDist(), b3.getDist()};

                    NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(
                            new TrilaterationFunction(positions, distances),
                            new LevenbergMarquardtOptimizer());
                    LeastSquaresOptimizer.Optimum optimum = solver.solve();

                    // the answer
                    centroid = optimum.getPoint().toArray();

                    v = new MyView(here);
                    setContentView(v);

                }
            }
        });

        ASBleScanner.startScan();
    }

    public double calculateDistance(double rssi){
        double ratio = rssi/K;
        if(ratio < 1.0) return Math.pow(ratio, 10);
        return (0.89976 * Math.pow(ratio, 7.7095) + 0.111);
    }

    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(final Canvas canvas) {

            canvas.drawCircle(b1.getLat(), b1.getLon(), b1.getDist(), pRange);
            canvas.drawCircle(b1.getLat(), b1.getLon(), 5, pBeacon);

            canvas.drawCircle(b2.getLat(), b2.getLon(), b2.getDist(), pRange);
            canvas.drawCircle(b2.getLat(), b2.getLon(), 5, pBeacon);

            canvas.drawCircle(b3.getLat(), b3.getLon(), b3.getDist(), pRange);
            canvas.drawCircle(b3.getLat(), b3.getLon(), 5, pBeacon);

            if(centroid != null)
                canvas.drawCircle((float) centroid[0], (float) centroid[1], 30, pSol);


        }
    }
}
