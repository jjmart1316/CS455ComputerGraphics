/*
   Course: CS 45500
   Name: Juan J Martinez
   Email: mart1316@pnw.edu
   Assignment: 0
*/

import framebuffer.FrameBuffer;
import framebuffer.FrameBuffer.Viewport;
import java.awt.Color;

/**


*/
public class Hw1 {
  public static void main(String[] args) {
    // Check for a file name on the command line.
    if (0 == args.length) {
      System.err.println("Usage: java Hw1 <PPM-file-name>");
      System.exit(-1);
    }

    // Your code goes here.
    // Create a framebuffer. Fill it with the checkerboard pattern.
    final int fbWidth = 1000;
    final int fbHeight = 600;
    FrameBuffer fb = new FrameBuffer(fbWidth, fbHeight);
    createCheckerPattern(fb);

    // Create a viewport and fill it with a flipped copy of the command-line PPM
    int upperX = 75;
    int upperY = 125;
    FrameBuffer fbImage = new FrameBuffer(args[0]);
    addImage(fb, upperX, upperY, flipVertical(fbImage), 0.0f);

    // Create another viewport and fill it with another flipped copy of the
    // command-line PPM file.

    upperX = 332;
    upperY = 125;
    fbImage = new FrameBuffer(args[0]);
    addImage(fb, upperX, upperY, flipHorizontal(fbImage), 0.0f);

    // Draw the striped pattern.
    upperX = 610;
    upperY = 420;
    diagonalLines(fb, upperX, upperY);

    // Create another viewport to hold a "framed" copy of the selected region.
    // Give this viewport a grayish background color.
    upperX = 725;
    upperY = 25;
    Color grayish = new Color(192, 192, 192);
    (fb.new Viewport(upperX, upperY, 250, 350)).clearVP(grayish);

    // Create another viewport inside the last one.
    // Copy the selected region's viewport into this last viewport.

    upperX = 501;
    upperY = 200;
    final int upperX2 = 750;
    final int upperY2 = 50;
    fb.new Viewport(upperX2, upperY2, fb.new Viewport(upperX, upperY, 199, 300));

    // Load Dumbledore into another FrameBuffer.
    // Create a viewport to hold Dumbledore's ghost.
    // Blend Dumbledore from the framebuffer into the viewport.
    upperX = 400;
    upperY = 100;
    fbImage = new FrameBuffer("Dumbledore.ppm");
    addImage(fb, upperX, upperY, fbImage, 0.3f);

    /******************************************/
    // Save the resulting image in a file.
    final String savedFileName = "Hw1.ppm";
    fb.dumpFB2File(savedFileName);
    System.err.println("Saved " + savedFileName);
  }

  public static void diagonalLines(FrameBuffer fb, int upperX, int upperY) {
    final Color blue = new Color(84, 129, 230);
    final Color pink = new Color(241, 95, 116);
    final Color green = new Color(152, 203, 74);
    final int w = 300;
    final int h = 120;

    final FrameBuffer tempfb = (fb.new Viewport(upperX, upperY, w, h)).convertVP2FB();

    final Viewport tempvp = tempfb.new Viewport(0, 0, w, 1);
    for (int x = 0; x < w; x++) {
      if (x % 90 > 60) {
        tempvp.setPixelVP(x, 0, blue);
      } else if (x % 90 > 30) {
        tempvp.setPixelVP(x, 0, green);
      } else {
        tempvp.setPixelVP(x, 0, pink);
      }
    }

    for (int y = 1; y < h; y++) {
      final Viewport xSample = tempfb.new Viewport(0, y - 1, w, 1);
      final Viewport xTarget = tempfb.new Viewport(0, y, w, 1);

      if (y % 90 < 30) {
        xTarget.setPixelVP(w - 1, 0, green);
      } else if (y % 90 < 60) {
        xTarget.setPixelVP(w - 1, 0, blue);
      } else {
        xTarget.setPixelVP(w - 1, 0, pink);
      }

      for (int x = 1; x < w; x++) {
        final Color c1 = xSample.getPixelVP(x, 0);
        xTarget.setPixelVP(x - 1, 0, c1);
      }
    }
    fb.new Viewport(upperX, upperY, tempfb);
  }

  public static void createCheckerPattern(FrameBuffer fb) {
    final Color c1 = new Color(255, 189, 96);
    final Color c2 = new Color(192, 56, 14);
    final int h = fb.getHeightFB();
    final int w = fb.getWidthFB();

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {

        if (x % 200 < 100 && y % 200 < 100) {
          fb.setPixelFB(x, y, c1);
        } else if (x % 200 > 100 && y % 200 > 100) {
          fb.setPixelFB(x, y, c1);
        } else {
          fb.setPixelFB(x, y, c2);
        }
      }
    }
  }

  public static FrameBuffer flipHorizontal(FrameBuffer fb) {
    final int h = fb.getHeightFB();
    final int w = fb.getWidthFB();

    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w / 2; x++) {

        Color leftPixel = fb.getPixelFB(x, y);
        Color rightPixel = fb.getPixelFB(w - 1 - x, y);
        fb.setPixelFB(x, y, rightPixel);
        fb.setPixelFB(w - 1 - x, y, leftPixel);
      }
    }
    return fb;
  }

  public static FrameBuffer flipVertical(FrameBuffer fb) {
    final int h = fb.getHeightFB();
    final int w = fb.getWidthFB();

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h / 2; y++) {

        Color upperPixel = fb.getPixelFB(x, y);
        Color lowerPixel = fb.getPixelFB(x, h - 1 - y);
        fb.setPixelFB(x, y, lowerPixel);
        fb.setPixelFB(x, h - 1 - y, upperPixel);
      }
    }
    return fb;
  }

  public static void addImage(FrameBuffer fb, int upperX, int upperY, FrameBuffer fbImage, float ratio) {
    final int w = fbImage.getWidthFB();
    final int h = fbImage.getHeightFB();
    final Viewport vp = fb.new Viewport(upperX, upperY, w, h);

    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {

        final Color c = fbImage.getPixelFB(x, y);
        if (isInsideTarget(c)) {
          final Color blendedColor = blendColor(c, vp.getPixelVP(x, y), ratio);
          vp.setPixelVP(x, y, blendedColor);
        }
      }
    }
  }

  public static Color blendColor(Color c1, Color c2, float ratio) {
    if (ratio > 1f) {
      ratio = 1f;
    } else if (ratio < 0f) {
      ratio = 0f;
    }
    
    float iRatio = 1.0f - ratio;

    int i1 = c1.getRGB();
    int i2 = c2.getRGB();

    int a1 = (i1 >> 24 & 0xff);
    int r1 = ((i1 & 0xff0000) >> 16);
    int g1 = ((i1 & 0xff00) >> 8);
    int b1 = (i1 & 0xff);

    int a2 = (i2 >> 24 & 0xff);
    int r2 = ((i2 & 0xff0000) >> 16);
    int g2 = ((i2 & 0xff00) >> 8);
    int b2 = (i2 & 0xff);

    int a = (int) ((a1 * iRatio) + (a2 * ratio));
    int r = (int) ((r1 * iRatio) + (r2 * ratio));
    int g = (int) ((g1 * iRatio) + (g2 * ratio));
    int b = (int) ((b1 * iRatio) + (b2 * ratio));

    return new Color(a << 24 | r << 16 | g << 8 | b);
  }

  public static boolean isInsideTarget(Color c) {
    return !(c.getRed() >= 252 && c.getGreen() >= 252 && c.getBlue() >= 251);
  }
}
