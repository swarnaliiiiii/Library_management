import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_imgcodecs.*;

import java.util.*;
import java.text.*;
import java.io.*;

import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

public class Library {
    public static void main(String[] args) throws IOException {
        Workbook wb = new Workbook();
        WritableSheet sheet1 = wb.createSheet("Sheet 1", 0);
        sheet1.addCell(new Label(0, 0, "S. No."));
        sheet1.addCell(new Label(1, 0, "Roll. No."));
        sheet1.addCell(new Label(2, 0, "Date"));
        sheet1.addCell(new Label(3, 0, "In Time"));
        sheet1.addCell(new Label(4, 0, "Book S. No."));
        sheet1.addCell(new Label(5, 0, "Out_Time"));
        wb.write();

        int count = 1;
        List<List<String>> lis = new ArrayList<>();
        boolean c = true;

        while (true) {
            if (lis.size() == 0) {
                List<String> cnt = new ArrayList<>();
                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                VideoCapture cap = new VideoCapture(0);
                if (!cap.isOpened()) {
                    System.out.println("Error: Camera not opened");
                    return;
                }

                Mat frame = new Mat();
                while (true) {
                    cap.read(frame);
                    opencv_imgproc.putText(frame, "Scan Barcode and press ESC", new Point(50, 50),
                            opencv_imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(255, 0, 0, 0), 2,
                            opencv_imgproc.LINE_AA, false);
                    imshow("frame", frame);
                    if (waitKey(30) == 27) {
                        imwrite("123.jpg", frame);
                        break;
                    }
                }
                cap.release();
                destroyAllWindows();

                String path = "123.jpg";
                BufferedImage image = ImageIO.read(new File(path));
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    String barcodeData = result.getText();
                    String barcodeType = result.getBarcodeFormat().toString();

                    String text2 = barcodeData + " (" + barcodeType + ")";
                    cnt.add(String.valueOf(count));
                    cnt.add(text2);
                    cnt.add(sdf.format(today));
                    lis.add(cnt);

                    System.out.println("-------------------------------");
                    System.out.println("You have entered your book");
                    System.out.println("Your Roll No:");
                    System.out.println(text2);
                    System.out.println("-------------------------------");
                } catch (NotFoundException e) {
                    System.err.println("Error: Barcode not found");
                }
            } else {
                c = true;
                List<String> cnt = new ArrayList<>();
                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                VideoCapture cap = new VideoCapture(0);
                if (!cap.isOpened()) {
                    System.out.println("Error: Camera not opened");
                    return;
                }

                Mat frame = new Mat();
                while (true) {
                    cap.read(frame);
                    opencv_imgproc.putText(frame, "Scan Barcode and press ESC", new Point(50, 50),
                            opencv_imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(255, 0, 0, 0), 2,
                            opencv_imgproc.LINE_AA, false);
                    imshow("frame", frame);
                    if (waitKey(30) == 27) {
                        imwrite("123.jpg", frame);
                        break;
                    }
                }
                cap.release();
                destroyAllWindows();

                String path = "123.jpg";
                BufferedImage image = ImageIO.read(new File(path));
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    String barcodeData = result.getText();
                    String barcodeType = result.getBarcodeFormat().toString();

                    String text2 = barcodeData + " (" + barcodeType + ")";

                    for (List<String> l : lis) {
                        if (l.get(1).equals(text2)) {
                            c = false;
                        }
                    }

                    if (c) {
                        cnt.add(String.valueOf(count));
                        cnt.add(text2);
                        cnt.add(sdf.format(today));
                        lis.add(cnt);

                        System.out.println("-------------------------------");
                        System.out.println("You have entered your book");
                        System.out.println("Roll No:");
                        System.out.println(text2);
                        System.out.println("-------------------------------");
                    } else {
                        System.out.println("Your entry closed successfully");
                    }
                } catch (NotFoundException e) {
                    System.err.println("Error: Barcode not found");
                }
            }
            count++;
        }
    }
}

