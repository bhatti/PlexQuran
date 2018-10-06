package com.plexobject.quran.utils;

public class FileTypeUtils {
	private static final String[][] TYPES = new String[][] {
			new String[] { "Text", "Files" },
			new String[] { ".doc", "Microsoft Word Document" },
			new String[] { ".docx", "Microsoft Word Open XML Document" },
			new String[] { ".log", "Log File" },
			new String[] { ".msg", "Outlook Mail Message" },
			new String[] { ".odt", "OpenDocument Text Document" },
			new String[] { ".pages", "Pages Document" },
			new String[] { ".rtf", "Rich Text Format File" },
			new String[] { ".tex", "LaTeX Source Document" },
			new String[] { ".txt", "Plain Text File" },
			new String[] { ".wpd", "WordPerfect Document" },
			new String[] { ".wps", "Microsoft Works Word Processor Document" },
			new String[] { "Data", "Files" },
			new String[] { ".csv", "Comma Separated Values File" },
			new String[] { ".dat", "Data File" },
			new String[] { ".efx", "eFax Document" },
			new String[] { ".epub", "Open eBook File" },
			new String[] { ".gbr", "Gerber File" },
			new String[] { ".ged", "GEDCOM Genealogy Data File" },
			new String[] { ".ibooks", "Multi-Touch iBook" },
			new String[] { ".key", "Keynote Presentation" },
			new String[] { ".pps", "PowerPoint Slide Show" },
			new String[] { ".ppt", "PowerPoint Presentation" },
			new String[] { ".pptx", "PowerPoint Open XML Presentation" },
			new String[] { ".sdf", "Standard Data File" },
			new String[] { ".tar", "Consolidated Unix File Archive" },
			new String[] { ".tax2010", "TurboTax 2010 Tax Return" },
			new String[] { ".vcf", "vCard File" },
			new String[] { ".xml", "XML File" },
			new String[] { "Audio", "Files" },
			new String[] { ".aif", "Audio Interchange File Format" },
			new String[] { ".iff", "Interchange File Format" },
			new String[] { ".m3u", "Media Playlist File" },
			new String[] { ".m4a", "MPEG-4 Audio File" },
			new String[] { ".mid", "MIDI File" },
			new String[] { ".mp3", "MP3 Audio File" },
			new String[] { ".mpa", "MPEG-2 Audio File" },
			new String[] { ".ra", "Real Audio File" },
			new String[] { ".wav", "WAVE Audio File" },
			new String[] { ".wma", "Windows Media Audio File" },
			new String[] { "Video", "Files" },
			new String[] { ".3g2", "3GPP2 Multimedia File" },
			new String[] { ".3gp", "3GPP Multimedia File" },
			new String[] { ".asf", "Advanced Systems Format File" },
			new String[] { ".asx", "Microsoft ASF Redirector File" },
			new String[] { ".avi", "Audio Video Interleave File" },
			new String[] { ".flv", "Flash Video File" },
			new String[] { ".mov", "Apple QuickTime Movie" },
			new String[] { ".mp4", "MPEG-4 Video File" },
			new String[] { ".mpg", "MPEG Video File" },
			new String[] { ".rm", "Real Media File" },
			new String[] { ".srt", "SubRip Subtitle File" },
			new String[] { ".swf", "Shockwave Flash Movie" },
			new String[] { ".vob", "DVD Video Object File" },
			new String[] { ".wmv", "Windows Media Video File" },
			new String[] { "3D", "Image Files" },
			new String[] { ".3dm", "Rhino 3D Model" },
			new String[] { ".max", "3ds Max Scene File" },
			new String[] { ".obj", "Wavefront 3D Object File" },
			new String[] { "Raster", "Image Files" },
			new String[] { ".bmp", "Bitmap Image File" },
			new String[] { ".dds", "DirectDraw Surface" },
			new String[] { ".dng", "Digital Negative Image File" },
			new String[] { ".gif", "Graphical Interchange Format File" },
			new String[] { ".jpg", "JPEG Image" },
			new String[] { ".png", "Portable Network Graphic" },
			new String[] { ".psd", "Adobe Photoshop Document" },
			new String[] { ".pspimage", "PaintShop Pro Image" },
			new String[] { ".tga", "Targa Graphic" },
			new String[] { ".thm", "Thumbnail Image File" },
			new String[] { ".tif", "Tagged Image File" },
			new String[] { ".yuv", "YUV Encoded Image File" },
			new String[] { "Vector", "Image Files" },
			new String[] { ".ai", "Adobe Illustrator File" },
			new String[] { ".eps", "Encapsulated PostScript File" },
			new String[] { ".ps", "PostScript File" },
			new String[] { ".svg", "Scalable Vector Graphics File" },
			new String[] { "Page", "Layout Files" },
			new String[] { ".indd", "Adobe InDesign Document" },
			new String[] { ".pct", "Picture File" },
			new String[] { ".pdf", "Portable Document Format File" },
			new String[] { "Spreadsheet", "Files" },
			new String[] { ".xlr", "Works Spreadsheet" },
			new String[] { ".xls", "Excel Spreadsheet" },
			new String[] { ".xlsx", "Microsoft Excel Open XML Spreadsheet" },
			new String[] { "Database", "Files" },
			new String[] { ".accdb", "Access 2007 Database File" },
			new String[] { ".db", "Database File" },
			new String[] { ".dbf", "Database File" },
			new String[] { ".mdb", "Microsoft Access Database" },
			new String[] { ".pdb", "Program Database" },
			new String[] { ".sql", "Structured Query Language Data File" },
			new String[] { "Executable", "Files" },
			new String[] { ".app", "Mac OS X Application" },
			new String[] { ".bat", "DOS Batch File" },
			new String[] { ".cgi", "Common Gateway Interface Script" },
			new String[] { ".com", "DOS Command File" },
			new String[] { ".exe", "Windows Executable File" },
			new String[] { ".gadget", "Windows Gadget" },
			new String[] { ".jar", "Java Archive File" },
			new String[] { ".pif", "Program Information File" },
			new String[] { ".vb", "VBScript File" },
			new String[] { ".wsf", "Windows Script File" },
			new String[] { "Game", "Files" },
			new String[] { ".dem", "Video Game Demo File" },
			new String[] { ".gam", "Saved Game File" },
			new String[] { ".nes", "Nintendo (NES) ROM File" },
			new String[] { ".rom", "N64 Game ROM File" },
			new String[] { ".sav", "Saved Game" },
			new String[] { "CAD", "Files" },
			new String[] { ".dwg", "AutoCAD Drawing Database File" },
			new String[] { ".dxf", "Drawing Exchange Format File" },
			new String[] { "GIS", "Files" },
			new String[] { ".gpx", "GPS Exchange File" },
			new String[] { ".kml", "Keyhole Markup Language File" },
			new String[] { "Web", "Files" },
			new String[] { ".asp", "Active Server Page" },
			new String[] { ".aspx", "Active Server Page Extended File" },
			new String[] { ".cer", "Internet Security Certificate" },
			new String[] { ".cfm", "ColdFusion Markup File" },
			new String[] { ".csr", "Certificate Signing Request File" },
			new String[] { ".css", "Cascading Style Sheet" },
			new String[] { ".htm", "Hypertext Markup Language File" },
			new String[] { ".html", "Hypertext Markup Language File" },
			new String[] { ".js", "JavaScript File" },
			new String[] { ".jsp", "Java Server Page" },
			new String[] { ".php", "PHP Source Code File" },
			new String[] { ".rss", "Rich Site Summary" },
			new String[] { ".xhtml",
					"Extensible Hypertext Markup Language File" },
			new String[] { "Plugin", "Files" },
			new String[] { ".plugin", "Mac OS X Plug-in" },
			new String[] { ".xll", "Excel Add-In File" },
			new String[] { "Font", "Files" },
			new String[] { ".fnt", "Windows Font File" },
			new String[] { ".fon", "Generic Font File" },
			new String[] { ".otf", "OpenType Font" },
			new String[] { ".ttf", "TrueType Font" },
			new String[] { "System", "Files" },
			new String[] { ".cab", "Windows Cabinet File" },
			new String[] { ".cpl", "Windows Control Panel Item" },
			new String[] { ".cur", "Windows Cursor" },
			new String[] { ".dll", "Dynamic Link Library" },
			new String[] { ".dmp", "Windows Memory Dump" },
			new String[] { ".icns", "Mac OS X Icon Resource File" },
			new String[] { ".ico", "Icon File" },
			new String[] { ".lnk", "File Shortcut" },
			new String[] { ".sys", "Windows System File" },
			new String[] { "Settings", "Files" },
			new String[] { ".cfg", "Configuration File" },
			new String[] { ".ini", "Windows Initialization File" },
			new String[] { ".keychain", "Mac OS X Keychain File" },
			new String[] { ".prf", "Outlook Profile File" },
			new String[] { "Encoded", "Files" },
			new String[] { ".hqx", "BinHex 4.0 Encoded File" },
			new String[] { ".mim", "Multi-Purpose Internet Mail Message File" },
			new String[] { ".uue", "Uuencoded File" },
			new String[] { "Compressed", "Files" },
			new String[] { ".7z", "7-Zip Compressed File" },
			new String[] { ".deb", "Debian Software Package" },
			new String[] { ".gz", "Gnu Zipped Archive" },
			new String[] { ".pkg", "Mac OS X Installer Package" },
			new String[] { ".rar", "WinRAR Compressed Archive" },
			new String[] { ".rpm", "Red Hat Package Manager File" },
			new String[] { ".sit", "StuffIt Archive" },
			new String[] { ".sitx", "StuffIt X Archive" },
			new String[] { ".tar.gz", "Compressed Tarball File" },
			new String[] { ".zip", "Zipped File" },
			new String[] { ".zipx", "Extended Zip File" },
			new String[] { "Disk", "Image Files" },
			new String[] { ".bin", "Binary Disc Image" },
			new String[] { ".cue", "Cue Sheet File" },
			new String[] { ".dmg", "Mac OS X Disk Image" },
			new String[] { ".iso", "Disc Image File" },
			new String[] { ".mdf", "Media Disc Image File" },
			new String[] { ".toast", "Toast Disc Image" },
			new String[] { ".vcd", "Virtual CD" },
			new String[] { "Developer", "Files" },
			new String[] { ".c", "C/C++ Source Code File" },
			new String[] { ".class", "Java Class File" },
			new String[] { ".cpp", "C++ Source Code File" },
			new String[] { ".cs", "Visual C# Source Code File" },
			new String[] { ".dtd", "Document Type Definition File" },
			new String[] { ".fla", "Adobe Flash Animation" },
			new String[] { ".java", "Java Source Code File" },
			new String[] { ".lua", "Lua Source File" },
			new String[] { ".m", "Objective-C Implementation File" },
			new String[] { ".pl", "Perl Script" },
			new String[] { ".py", "Python Script" },
			new String[] { ".sln", "Visual Studio Solution File" },
			new String[] { ".vcxproj", "Visual C++ Project" },
			new String[] { "Backup", "Files" },
			new String[] { ".bak", "Backup File" },
			new String[] { ".tmp", "Temporary File" },
			new String[] { "Misc", "Files" },
			new String[] { ".dbx", "Outlook Express E-mail Folder" },
			new String[] { ".ics", "iCalendar File" },
			new String[] { ".msi", "Windows Installer Package" },
			new String[] { ".part", "Partially Downloaded File" },
			new String[] { ".torrent", "BitTorrent File" } };

	public static String getType(String name) {
		String lname = name.toLowerCase();
		String kind = "";
		for (String[] exts : TYPES) {
			if (exts[1].equals("Files")) {
				kind = exts[0];
			} else if (lname.endsWith(exts[0])) {
				return kind + " - " + exts[1];
			}
		}
		return "";
	}

	public static String getKind(String name) {
		String lname = name.toLowerCase();
		String kind = "";
		for (String[] exts : TYPES) {
			if (exts[1].equals("Files")) {
				kind = exts[0];
			} else if (lname.endsWith(exts[0])) {
				return kind.toLowerCase();
			}
		}
		return "";
	}
}