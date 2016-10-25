package org.apache.sshd;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.subsystem.sftp.SftpFileSystemProvider;
import org.apache.sshd.common.BaseBuilder;
import org.apache.sshd.common.signature.BuiltinSignatures;

public class WataTest {

	public static void main(String[] args) throws Exception {
		System.out.println("================");
		BuiltinSignatures.nistp256.isSupported();
		
		// Direct URI
	    Path remotePath = Paths.get(new URI("sftp://root:vimicro@10.150.10.111"));
	    Files.walk(remotePath).forEach(p->System.out.println(p));

//	    // "Mounting" a file system
//	    URI uri = SftpFileSystemProvider.createFileSystemURI(host, port, username, password);
//	    try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap())) {
//	        Path remotePath = fs.getPath("/some/remote/path");
//	        ...
//	    }
//
//	    // Full programmatic control
//	    SshClient client = ...setup and start the SshClient instance...
//	    SftpFileSystemProvider provider = new SftpFileSystemProvider(client);
//	    URI uri = SftpFileSystemProvider.createFileSystemURI(host, port, username, password);
//	    try (FileSystem fs = provider.newFileSystem(uri, Collections.<String, Object>emptyMap())) {
//	        Path remotePath = fs.getPath("/some/remote/path");
//	    }
		
		
		
		
		
		
		
	}

}
