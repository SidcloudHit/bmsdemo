/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import com.aspose.cells.FileFormatInfo;
import com.aspose.cells.FileFormatUtil;
import com.aspose.cells.MsoDrawingType;
import com.aspose.cells.OleObject;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import java.io.InputStream;

/**
 *
 * @author NIC
 */
public class secure_xlfile_validation implements DocumentDetector {

    private static final List<String> ALLOWED_FORMAT
            = Arrays.asList(new String[]{"xls", "xlsx", "xlsm", "xlsb", "xlt", "xltm"});

    

    @Override
    public boolean isSafe(InputStream ist) {
        boolean safeState = false;
        try {
            if ((ist != null)) {
                // Perform a first check on Excel document format
                
                
                FileFormatInfo formatInfo = FileFormatUtil.detectFileFormat(ist);
                String formatExtension = FileFormatUtil.loadFormatToExtension(formatInfo.getLoadFormat());
                if ((formatExtension != null)
                        && ALLOWED_FORMAT.contains(formatExtension.toLowerCase(Locale.US).replaceAll("\\.", ""))) {
                    // Load the file into the Excel document parser
                    Workbook book = new Workbook(ist);
                    
                    // Get safe state from Macro presence
                    safeState = !book.hasMacro();
                    // If document is safe then we pass to OLE objects analysis
                    if (safeState) {
                        // Search OLE objects in all workbook sheets
                        Worksheet sheet = null;
                        OleObject oleObject = null;
                        int totalOLEObjectCount = 0;
                        for (int i = 0; i < book.getWorksheets().getCount(); i++) {
                            sheet = book.getWorksheets().get(i);
                            for (int j = 0; j < sheet.getOleObjects().getCount(); j++) {
                                oleObject = sheet.getOleObjects().get(j);
                                if (oleObject.getMsoDrawingType() == MsoDrawingType.OLE_OBJECT) {
                                    totalOLEObjectCount++;
                                }
                            }
                        }
                        // Update safe status flag according to number of OLE object found
                        if (totalOLEObjectCount != 0) {
                            safeState = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            safeState = false;
            //LOG.warn("Error during Pdf file analysis !", e);
        }
        return safeState;
    }
}