package com.iot.cloud.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;


/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
public class PDFUtil {
    public static void main(String[] args) throws Exception {

        String FILE_DIR = "E:\\";
        //Step 1—Create a Document.
        Document document = new Document();
        //Step 2—Get a PdfWriter instance.
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "createSamplePDF.pdf"));
        addHeaderAndFooter(pdfWriter);
        //Step 3—Open the Document.
        document.open();
        addLine(document);
        //Step 4—Add content.
        document.add(new Paragraph("Hello World"));
        addImage(document);
        List<String> headList=new ArrayList<String>();
        headList.add("one");
        headList.add("two");
        headList.add("three");
        List<Object> dataList=new ArrayList<Object>();

        addTable(document,headList,dataList);

        addLine(document);
        //Step 5—Close the Document.
        document.close();

    }

    /**
     * 添加表格
     * @param headList
     * @param dataList
     */
    private static void addTable(Document document,List<String> headList, List<Object> dataList) throws Exception {
        // 设置中文字体
        BaseFont bfChinese =BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);
        // 建立一个pdf表格
        PdfPTable table = new PdfPTable(headList.size());
        //设置header
        for(int i=0;i<headList.size();i++){
            PdfPCell cell =  new PdfPCell(new Paragraph(headList.get(i),fontChinese));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
        //设置数据


        document.add(table);
    }

    /**
     * 画直线
     * @param document
     * @throws Exception
     */
    private static void addLine(Document document) throws Exception {
        //直线
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(new LineSeparator()));
        document.add(p1);
    }

    /**
     * 设置头和底部
     * @param pdfWriter
     */
    private static void addHeaderAndFooter(PdfWriter pdfWriter) {
        pdfWriter.setPageEvent(new PdfPageEventHelper() {

            public void onEndPage(PdfWriter writer, Document document) {

                PdfContentByte cb = writer.getDirectContent();
                cb.saveState();
                cb.beginText();
                BaseFont bf = null;
                try {
                    //解决中文乱码
                    bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cb.setFontAndSize(bf, 10);
                //Header
                float x = document.top(-20);
                //左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                        "H-Left",
                        document.left(), x, 0);
                //中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                        writer.getPageNumber() + " page",
                        (document.right() + document.left()) / 2,
                        x, 0);
                //右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                        "H-Right",
                        document.right(), x, 0);

                //Footer
                float y = document.bottom(-20);
                //左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                        "DIALux",
                        document.left(), y, 0);
                //中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                        " 利达信 有好光",
                        (document.right() + document.left())/2,
                        y, 0);
                //右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                        "页 " + writer.getPageNumber(),
                        document.right(), y, 0);

                cb.endText();
                cb.restoreState();
            }
        });
    }

    /**
     * 插入图片
     *
     * @param document
     * @throws Exception
     */
    private static void addImage(Document document) throws Exception {
        Image image = Image.getInstance(new URL("https://goss.veer.com/creative/vcg/veer/612/veer-157609439.jpg"));
        float[] widths = {6f};

        PdfPTable table = new PdfPTable(widths);

        //插入图片
//        table.addCell(new PdfPCell(new Paragraph("图片测试")));
//        table.addCell(image);
        //调整图片大小
//        table.addCell("This two");
        table.addCell(new PdfPCell(image, true));
        table.addCell(new PdfPCell(image, true));
        table.addCell(new PdfPCell(image, true));
        //不调整
//        table.addCell("This three");
//        table.addCell(new PdfPCell(image, false));
        document.add(table);
    }


}
