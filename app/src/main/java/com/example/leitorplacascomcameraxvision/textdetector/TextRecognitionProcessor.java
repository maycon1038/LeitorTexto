

package com.example.leitorplacascomcameraxvision.textdetector;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.leitorplacascomcameraxvision.GraphicOverlay;
import com.example.leitorplacascomcameraxvision.VisionProcessorBase;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.Text.Element;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import java.util.List;

public class TextRecognitionProcessor extends VisionProcessorBase<Text> {

  private static final String TAG = "TextRecProcessor";

  private final TextRecognizer textRecognizer;

  public TextRecognitionProcessor(Context context) {
    super(context);
    textRecognizer = TextRecognition.getClient();
  }

  @Override
  public void stop() {
    super.stop();
    textRecognizer.close();
  }

  @Override
  protected Task<Text> detectInImage(InputImage image) {
    return textRecognizer.process(image);
  }

  @Override
  protected void onSuccess(@NonNull Text text, @NonNull GraphicOverlay graphicOverlay) {
    Log.d(TAG, "Detecção de texto no dispositivo bem-sucedida");
    logExtrasForTesting(text);
    graphicOverlay.add(new TextGraphic(graphicOverlay, text));
  }

  private static void logExtrasForTesting(Text text) {
    if (text != null) {
      Log.v(MANUAL_TESTING_LOG, "O texto detectado tem : " + text.getTextBlocks().size() + " blocks");
      for (int i = 0; i < text.getTextBlocks().size(); ++i) {
        List<Text.Line> lines = text.getTextBlocks().get(i).getLines();
        Log.v(
            MANUAL_TESTING_LOG,
            String.format("Bloco de texto detectado %d tem %d linhas", i, lines.size()));
        for (int j = 0; j < lines.size(); ++j) {
          List<Element> elements = lines.get(j).getElements();
          Log.v(
              MANUAL_TESTING_LOG,
              String.format("Linha de texto detectada %d tem %d elementos", j, elements.size()));
          for (int k = 0; k < elements.size(); ++k) {
            Element element = elements.get(k);
            Log.v(
                MANUAL_TESTING_LOG,
                String.format("Elemento de texto detectado %d diz: %s", k, element.getText()));
            Log.v(
                MANUAL_TESTING_LOG,
                String.format(
                    "Elemento de texto detectado %d tem uma caixa delimitadora: %s",
                    k, element.getBoundingBox().flattenToString()));
            Log.v(
                MANUAL_TESTING_LOG,
                String.format(
                    "O tamanho esperado do ponto de canto é 4, obter %d", element.getCornerPoints().length));
            for (Point point : element.getCornerPoints()) {
              Log.v(
                  MANUAL_TESTING_LOG,
                  String.format(
                      "Ponto de canto para elemento %d está localizado em: x - %d, y = %d",
                      k, point.x, point.y));
            }
          }
        }
      }
    }
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.w(TAG, "Text detection failed." + e);
  }
}
