package devcom.marcos.triangularpos;


import androidx.appcompat.app.AppCompatActivity;
import devcom.marcos.triangularpos.trilateration.Point;
import devcom.marcos.triangularpos.trilateration.Trilateration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import static devcom.marcos.triangularpos.trilateration.Trilateration.Compute;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View vista = new MyView(this);
        setContentView(vista);


    }

    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setStrokeWidth(60);
            p.setColor(Color.BLUE);

            canvas.drawCircle(100, 100, 30, p);
            canvas.drawCircle(200, 200, 30, p);
            canvas.drawCircle(300, 100, 30, p);

            Point p1 = new Point(100,100,100);
            Point p2 = new Point(200,200,100);
            Point p3 = new Point(300,100,100);

            double[][] positions = new double[][] { { 100.0, 100.0 }, { 200.0, 200.0 },
                    { 300.0, 100.0 } };
            double[] distances = new double[] { 100.0, 100.0, 100.0 };

            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(
                    new TrilaterationFunction(positions, distances),
                    new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            // the answer
            double[] centroid = optimum.getPoint().toArray();

            // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
            RealVector standardDeviation = optimum.getSigma(0);
            RealMatrix covarianceMatrix = optimum.getCovariances(0);

            canvas.drawCircle((float)centroid[0], (float)centroid[1], 30, p);
        }
    }
}
