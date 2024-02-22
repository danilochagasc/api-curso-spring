package br.com.erudio.apicurso.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import br.com.erudio.apicurso.config.FileStorageConfig;
import br.com.erudio.apicurso.exception.FileStorageException;
import br.com.erudio.apicurso.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;



@Service
public class FileStorageService {

	//caminho ate a pasta onde os arquivos serao armazenados
	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir())
			.toAbsolutePath().normalize();
		
		this.fileStorageLocation = path;

		//se o diretorio passado no arquivo de config
		// existe ai vc usa ele, se nao, cria ele sozinho.
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException(
				"Could not create the directory where the uploaded files will be stored!", e);
		}
	}
	//service responsavel por armazenar os arquivos
	public String storeFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Filename..txt
			if (filename.contains("..")) {
				throw new FileStorageException(
					"Sorry! Filename contains invalid path sequence " + filename);
			}

			//linhas responsaveis por decidir o que sera feito com o arquivo em caso ele ja exista
			//ou outras possiveis configuracoes
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return filename;
		} catch (Exception e) {
			throw new FileStorageException(
				"Could not store file " + filename + ". Please try again!", e);
		}
	}
	
	public Resource loadFileAsResource(String filename) {
		try {
			Path filePath = this.fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) return resource;
			else throw new MyFileNotFoundException("File not found");
		} catch (Exception e) {
			throw new MyFileNotFoundException("File not found" + filename, e);
		}
	}

}
