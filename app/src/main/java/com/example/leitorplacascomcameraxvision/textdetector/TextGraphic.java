/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.leitorplacascomcameraxvision.textdetector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.leitorplacascomcameraxvision.GraphicOverlay;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.Text.Element;
import com.google.mlkit.vision.text.Text.Line;
import com.google.mlkit.vision.text.Text.TextBlock;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class TextGraphic extends GraphicOverlay.Graphic {

    private static final String TAG = "TextGraphic";

    private static final int TEXT_COLOR = Color.BLACK;
    private static final int MARKER_COLOR = Color.WHITE;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;
    private final Paint rectPaint;
    private final Paint textPaint;
    private final Paint labelPaint;
    private final Text text;
    private final String regex = "[A-Z]{3}[0-9]{4}|[A-Z]{3}[0-9][[A-J]?|[0-9]?][0-9]{2}";
    private final Pattern pattern;

    TextGraphic(GraphicOverlay overlay, Text text) {
        super(overlay);
        pattern = Pattern.compile(regex);
        this.text = text;

        rectPaint = new Paint();
        rectPaint.setColor(MARKER_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);

        labelPaint = new Paint();
        labelPaint.setColor(MARKER_COLOR);
        labelPaint.setStyle(Paint.Style.FILL);
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "O texto é: " + text.getText());

        for (TextBlock textBlock : text.getTextBlocks()) {
            // Processa o texto na parte inferior da caixa.
            Log.d(TAG, "Texto TextBlock é: " + textBlock.getText());
            Log.d(TAG, "A caixa delimitadora TextBlock é: " + textBlock.getBoundingBox());
            Log.d(TAG, "O ponto de canto do TextBlock é: " + Arrays.toString(textBlock.getCornerPoints()));
            Matcher matcher = pattern.matcher(unmaskdPlaca(textBlock.getText()));
            if (matcher.find() && (textBlock.getText().length() == 7 || textBlock.getText().length() == 8)) {


                for (Line line : textBlock.getLines()) {
                    Log.d(TAG, "O texto da linha é: " + line.getText());
                    Log.d(TAG, "A caixa delimitadora de linha é: " + line.getBoundingBox());
                    Log.d(TAG, "O ponto de canto da linha é: " + Arrays.toString(line.getCornerPoints()));
                    // Draws the bounding box around the TextBlock.
                    RectF rect = new RectF(line.getBoundingBox());
                    // If the image is flipped, the left will be translated to right, and the right to left.
                    float x0 = translateX(rect.left);
                    float x1 = translateX(rect.right);
                    rect.left = min(x0, x1);
                    rect.right = max(x0, x1);
                    rect.top = translateY(rect.top);
                    rect.bottom = translateY(rect.bottom);
                    canvas.drawRect(rect, rectPaint);

                    float lineHeight = TEXT_SIZE + 2 * STROKE_WIDTH;
                    float textWidth = textPaint.measureText(line.getText());
                    canvas.drawRect(
                            rect.left - STROKE_WIDTH,
                            rect.top - lineHeight,
                            rect.left + textWidth + 2 * STROKE_WIDTH,
                            rect.top,
                            labelPaint);
                    // Renders the text at the bottom of the box.
                    canvas.drawText(line.getText(), rect.left, rect.top - STROKE_WIDTH, textPaint);

                    for (Element element : line.getElements()) {
                        Log.d(TAG, "O texto do elemento é: " + element.getText());
                        Log.d(TAG, "A caixa delimitadora do elemento é: " + element.getBoundingBox());
                        Log.d(TAG, "O ponto de canto do elemento é: " + Arrays.toString(element.getCornerPoints()));
                        Log.d(TAG, "A linguagem do elemento é: " + element.getRecognizedLanguage());
                    }
                }

            }
        }
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
