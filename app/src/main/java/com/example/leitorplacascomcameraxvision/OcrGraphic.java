
package com.example.leitorplacascomcameraxvision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;


import com.google.mlkit.vision.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrGraphic extends GraphicOverlay.Graphic {

    private int mId;

    private static  int TEXT_COLOR = Color.WHITE;

    private static  int RECT_COLOR = Color.BLACK;
	private String regex = "[A-Z]{3}[0-9]{4}|[A-Z]{3}[0-9][[A-J]?|[0-9]?][0-9]{2}";
	private Pattern pattern;
    protected static Paint sRectPaint;
    protected static Paint sTextPaint;
    private final Text.TextBlock mText;
	private static final float TEXT_SIZE = 25.0f;
    public OcrGraphic(GraphicOverlay overlay, Text.TextBlock text) {
        super(overlay);

        mText = text;

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(RECT_COLOR);
			sRectPaint.setStyle(Paint.Style.STROKE);
			sRectPaint.setStrokeWidth(8.0f);
        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(TEXT_SIZE);
        }
		pattern = Pattern.compile(regex);
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Text.TextBlock getTextBlock() {
        return mText;
    }

    public boolean contains(float x, float y) {
        Text.TextBlock text = mText;
        if (text == null) {
            return false;
        }
        RectF rect = new RectF(text.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }


    @Override
    public void draw(Canvas canvas) {
        Text.TextBlock text = mText;
		if (text == null) {
			return;
		}


	/*	Matcher matcher = pattern.matcher(unmaskdPlaca(text.getValue()));

		if (matcher.find() && (text.getValue().length() == 7 || text.getValue().length() == 8)) {
			//INICIAR PROCESSO DE BUSCAR VEÍCULOS SUSPEITOS
			final String result = matcher.group();
			if (result.length() == 7) {
				// Draws the bounding box around the text.
				final RectF rect = new RectF(text.getBoundingBox());

				//retorna o texto extraído
				//	s = itextBlock.setTextBlock(rect, result);

				rect.left = translateX(rect.left);
				rect.top = translateY(rect.top);
				rect.right = translateX(rect.right);
				rect.bottom = translateY(rect.bottom);
				canvas.drawRect(rect, sRectPaint);

				float with = (rect.right - rect.left);
				float heith = (rect.bottom - rect.top);
				if (with / 2 > heith) {
					//carro
					sTextPaint.setTextSize(heith - 15.0f);
					canvas.drawText(addChar(result, '-', 3), rect.left + 5.f, rect.bottom - 10.f, sTextPaint);
				} else {
					//moto
					sTextPaint.setTextSize((heith / 2) - 5.0f);
					canvas.drawText(result.substring(0, 3), rect.left + 20.f, rect.bottom - (heith / 2) - 10.f, sTextPaint);
					canvas.drawText(result.substring(3, 7), rect.left + 5.f, rect.bottom - 10.f, sTextPaint);
				}

			}
		}*/

	}

	private String addChar(String str, char ch, int position) {
		return str.substring(0, position) + ch + str.substring(position);
	}

	private String unmaskdPlaca(String s) {
		return s.replace("[.]", "").replace("[-]", "")
				.replaceAll(" ", "")
				.replace("\n", "");
	}
}
