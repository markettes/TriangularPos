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

            canvas.drawCircle(400, 300, 30, p);
            canvas.drawCircle(500, 650, 30, p);
            canvas.drawCircle(651, 350, 30, p);

            Point p1 = new Point(400,300,150);
            Point p2 = new Point(500,650,250);
            Point p3 = new Point(651,350,235);

            double[] pf = Trilateration.Compute(p1,p2,p3);

            canvas.drawCircle((float)pf[0], (float)pf[1], 30, p);

        }
    }
}
