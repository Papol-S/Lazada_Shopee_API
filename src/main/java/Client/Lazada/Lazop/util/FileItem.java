package Client.Lazada.Lazop.util;

import java.io.*;

/**
 * File wrapper. It support local file, byte array and input stream parameters.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class FileItem {

	private Contract contract;

	/**
	 * Local file object constructor.
	 */
	public FileItem(final File file) {
		this.contract = new LocalContract(file);
	}

	/**
	 * Local file path constructor.
	 */
	public FileItem(String filePath) {
		this(new File(filePath));
	}

	/**
	 * Byte array constructor.
	 */
	public FileItem(String fileName, byte[] content) {
		this(fileName, content, null);
	}

	/**
	 * Byte array constructor with specified mime type.
	 * 
	 * @param fileName file name
	 * @param content file content
	 * @param mimeType file type, e.g. image/jpeg, text/plain
	 */
	public FileItem(String fileName, byte[] content, String mimeType) {
		this.contract = new ByteArrayContract(fileName, content, mimeType);
	}

	/**
	 * Input stream constructor.
	 */
	public FileItem(String fileName, InputStream stream) {
		this(fileName, stream, null);
	}

	/**
	 * Input stream constructor with specified mime type.
	 * 
	 * @param fileName file name
	 * @param stream file stream
	 * @param mimeType file type，eg：image/jpeg, text/plain
	 */
	public FileItem(String fileName, InputStream stream, String mimeType) {
		this.contract = new StreamContract(fileName, stream, mimeType);
	}

	public boolean isValid() {
		return this.contract.isValid();
	}

	public String getFileName() {
		return this.contract.getFileName();
	}

	public String getMimeType() throws IOException {
		return this.contract.getMimeType();
	}

	public long getFileLength() {
		return this.contract.getFileLength();
	}

	public void write(OutputStream output) throws IOException {
		this.contract.write(output);
	}

	private static interface Contract {
		public boolean isValid();

		public String getFileName();

		public String getMimeType();

		public long getFileLength();

		public void write(OutputStream output) throws IOException;
	}

	private static class LocalContract implements Contract {
		private File file;

		public LocalContract(File file) {
			this.file = file;
		}

		public boolean isValid() {
			return this.file != null && this.file.exists() && this.file.isFile();
		}

		public String getFileName() {
			return this.file.getName();
		}

		public String getMimeType() {
			return Constants.MIME_TYPE_DEFAULT;
		}

		public long getFileLength() {
			return this.file.length();
		}

		public void write(OutputStream output) throws IOException {
			InputStream input = null;
			try {
				input = new FileInputStream(this.file);
				byte[] buffer = new byte[Constants.READ_BUFFER_SIZE];
				int n = 0;
				while (-1 != (n = input.read(buffer))) {
					output.write(buffer, 0, n);
				}
			} finally {
				if (input != null) {
					input.close();
				}
			}
		}
	}

	private static class ByteArrayContract implements Contract {
		private String fileName;
		private byte[] content;
		private String mimeType;

		public ByteArrayContract(String fileName, byte[] content, String mimeType) {
			this.fileName = fileName;
			this.content = content;
			this.mimeType = mimeType;
		}

		public boolean isValid() {
			return this.content != null && this.fileName != null;
		}

		public String getFileName() {
			return this.fileName;
		}

		public String getMimeType() {
			if (this.mimeType == null) {
				return Constants.MIME_TYPE_DEFAULT;
			} else {
				return this.mimeType;
			}
		}

		public long getFileLength() {
			return this.content.length;
		}

		public void write(OutputStream output) throws IOException {
			output.write(this.content);
		}
	}

	private static class StreamContract implements Contract {
		private String fileName;
		private InputStream stream;
		private String mimeType;

		public StreamContract(String fileName, InputStream stream, String mimeType) {
			this.fileName = fileName;
			this.stream = stream;
			this.mimeType = mimeType;
		}

		public boolean isValid() {
			return this.stream != null && this.fileName != null;
		}

		public String getFileName() {
			return this.fileName;
		}

		public String getMimeType() {
			if (this.mimeType == null) {
				return Constants.MIME_TYPE_DEFAULT;
			} else {
				return this.mimeType;
			}
		}

		public long getFileLength() {
			return 0L;
		}

		public void write(OutputStream output) throws IOException {
			try {
				byte[] buffer = new byte[Constants.READ_BUFFER_SIZE];
				int n = 0;
				while (-1 != (n = stream.read(buffer))) {
					output.write(buffer, 0, n);
				}
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		}
	}

}
