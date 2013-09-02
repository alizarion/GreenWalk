package com.project.web.servlets;


import com.project.entities.ImageContentFile;
import com.project.entities.UploadedFile;
import com.project.entities.VideoContentFile;
import com.project.services.EntityFacade;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 25/11/11
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(urlPatterns = "/images/dynamic/*")
public class DynamicImageServlet extends HttpServlet {

    @EJB
    EntityFacade facade;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
            ,FileNotFoundException

    {

        try {

            // Get image file.

            String fileId = request.getParameter("file");

            Integer width =  (request.getParameter("width") != null ?
                    Integer.parseInt(request.getParameter("width")) : null);
            Integer height =  (request.getParameter("height") != null ?
                    Integer.parseInt(request.getParameter("height")) : null);
            String hashControl = request.getParameter("hctrl");

            if(!fileId.isEmpty()){
                UploadedFile upFile=  facade.getUploadedFileById(Long.parseLong(fileId));
                if (upFile== null){
                    return;
                }

                if(!upFile.getHashControl().equals(hashControl)){
                    return;
                }
                // String filePath = upFile.getFullPath();
                File file =  upFile.getFileFullPath();
                if(!file.exists()){
                    return;
                }
                if (upFile instanceof ImageContentFile){
                    String extend = upFile.getFileName().substring(upFile.getFileName().lastIndexOf("."),upFile.getFileName().length());
                    if (extend != null){
                        if (extend.equalsIgnoreCase(".gif")){

                            ImageContentFile image = (ImageContentFile) upFile;
                            if (image.getHeight() != null){
                                BufferedInputStream in = new BufferedInputStream(
                                        new FileInputStream(file));

                                // Get image contents.
                                byte[] bytes = new byte[in.available()];

                                in.read(bytes);
                                in.close();

                                // Write image contents to response.
                                response.getOutputStream().write(bytes);
                            }  else {
                                checkImageSize(upFile);
                                BufferedInputStream in = new BufferedInputStream(
                                        new FileInputStream(file));

                                // Get image contents.
                                byte[] bytes = new byte[in.available()];

                                in.read(bytes);
                                in.close();

                                // Write image contents to response.
                                response.getOutputStream().write(bytes);
                            }
                        }
                    }
                }
                if (width != null && height != null){
                    if (upFile.getResizedFileFullPath(width,height).exists()){
                        BufferedInputStream in = new BufferedInputStream(
                                upFile.readResizedFile(width,height));
                        checkImageSize(upFile);
                        // Get image contents.
                        byte[] bytes = new byte[in.available()];

                        in.read(bytes);
                        in.close();

                        // Write image contents to response.
                        response.getOutputStream().write(bytes);
                    }   else {

                        InputStream in =upFile.readFile();
                        BufferedImage image;
                        try {
                            image = Sanselan.getBufferedImage(upFile.getFileFullPath());
                        } catch (ImageReadException e) {
                            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
                            image = decoder.decodeAsBufferedImage();
                        }


                        String extension;
                        //int dotPos = upFile.getFileName().lastIndexOf(".");
                        // extension = upFile.getFileName().substring(dotPos+1);
                        OutputStream out = response.getOutputStream();
                        if (image.getWidth() > width){

                            image = scaleImage(upFile,image, image.getType(),width,height);

                            // DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
                            ImageIO.write(image, "jpg" ,
                                    new File(file.getParent(),width + "_" + height+"_" + file.getName()));

                        }
                        ImageIO.write(image, "jpg" , out);
                        out.close();

                    }
                    // response.getOutputStream().write(buffer.getData());
                }   else {
                    //     file = FileHelper.CONTEXT_DIRECTORY_PATH + "\\" +file;

                    BufferedInputStream in = new BufferedInputStream(upFile.readFile());

                    // Get image contents.
                    byte[] bytes = new byte[in.available()];

                    in.read(bytes);
                    in.close();

                    // Write image contents to response.
                    response.getOutputStream().write(bytes);
                }

            }
        } catch (IOException e) {

            // e.printStackTrace();

        }

    }
    private  BufferedImage resizeImage(BufferedImage image, float coeffLargeur, float coeffHauteur)
    {
        AffineTransform tx = new AffineTransform();

        tx.scale(coeffLargeur, coeffHauteur);

        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BICUBIC);

        BufferedImage newImage = new BufferedImage((int)(image.getWidth(null)*coeffLargeur),
                (int)(image.getHeight(null)*coeffHauteur),image.getType());

        return op.filter(image, newImage);
    }

    private void checkImageSize(UploadedFile uploadedFile) throws IOException {



        if(uploadedFile instanceof ImageContentFile ){
            ImageContentFile contentFile = (ImageContentFile)uploadedFile;
            if (contentFile.getHeight() == null){
                InputStream in = new FileInputStream(uploadedFile.getFileFullPath());
                BufferedImage image;
                try {
                    image = Sanselan.getBufferedImage(uploadedFile.getFileFullPath());
                } catch (ImageReadException e) {
                    JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
                    image = decoder.decodeAsBufferedImage();
                }
                contentFile.setHeight(new Integer(image.getHeight()).toString());
                contentFile.setWidth(new Integer(image.getWidth()).toString());
                facade.updateUploadedFile(contentFile);
            }
        }

        if(uploadedFile instanceof VideoContentFile ){
            VideoContentFile contentFile = (VideoContentFile)uploadedFile;
            if (contentFile.getHeight() == null){
                InputStream in = new FileInputStream(uploadedFile.getFileFullPath());
                BufferedImage image;
                try {
                    image = Sanselan.getBufferedImage(uploadedFile.getFileFullPath());
                } catch (ImageReadException e) {
                    JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
                    image = decoder.decodeAsBufferedImage();
                }
                contentFile.setHeight(image.toString());
                contentFile.setWidth(image.toString());
                facade.updateUploadedFile(contentFile);
            }
        }
    }

    private  BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height,
                BufferedImage.SCALE_DEFAULT);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
    private  BufferedImage scaleImage(UploadedFile uploadedFile, BufferedImage image, int imageType,
                                      int newWidth, int newHeight) {
        // Make sure the aspect ratio is maintained, so the image is not distorted
        double thumbRatio = (double) newWidth / (double) newHeight;
        Integer imageWidth = image.getWidth(null);
        Integer imageHeight = image.getHeight(null);
        if(uploadedFile instanceof ImageContentFile ){
            ImageContentFile contentFile = (ImageContentFile)uploadedFile;
            if (contentFile.getHeight() == null){
                contentFile.setHeight(imageHeight.toString());
                contentFile.setWidth(imageWidth.toString());
                facade.updateUploadedFile(contentFile);
            }
        }

        if(uploadedFile instanceof VideoContentFile){
            VideoContentFile contentFile = (VideoContentFile)uploadedFile;
            if (contentFile.getHeight() == null){
                contentFile.setHeight(imageHeight.toString());
                contentFile.setWidth(imageWidth.toString());
                facade.updateUploadedFile(contentFile);
            }
        }
        double aspectRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < aspectRatio) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }
        // Draw the scaled image
        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
                imageType);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);

        return newImage;
    }
}