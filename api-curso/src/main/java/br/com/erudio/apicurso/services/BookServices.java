package br.com.erudio.apicurso.services;

import br.com.erudio.apicurso.controller.BookController;
import br.com.erudio.apicurso.converter.DozerConverter;
import br.com.erudio.apicurso.data.model.Book;
import br.com.erudio.apicurso.data.vo.v1.BookVO;
import br.com.erudio.apicurso.exception.RequiredObjectIsNullException;
import br.com.erudio.apicurso.exception.ResourceNotFoundException;
import br.com.erudio.apicurso.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Service
public class BookServices {
	
	@Autowired
	BookRepository repository;

	@Autowired
	PagedResourcesAssembler<BookVO> assembler;

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

		var bookPage = repository.findAll(pageable);

		var bookVosPage = bookPage.map(b-> DozerConverter.parseObject(b, BookVO.class));
		bookVosPage.map(
				b-> b.add(
						linkTo(methodOn(BookController.class)
								.findById(b.getKey())).withSelfRel()));

		Link link = linkTo(
				methodOn(BookController.class)
						.findAll(pageable.getPageNumber(),
								pageable.getPageSize(),
								"asc")).withSelfRel();

		return assembler.toModel(bookVosPage,link);
	}
		
	public BookVO create(BookVO book) {

		if (book == null) throw new RequiredObjectIsNullException();

		var entity = DozerConverter.parseObject(book, Book.class);
		var vo = DozerConverter.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public BookVO findById(Long id) {

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		BookVO vo = DozerConverter.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
		
	public BookVO update(BookVO book) {

		if (book == null) throw new RequiredObjectIsNullException();

		var entity = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo = DozerConverter.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}	
	
	public void delete(Long id) {
		Book entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		repository.delete(entity);
	}

}
