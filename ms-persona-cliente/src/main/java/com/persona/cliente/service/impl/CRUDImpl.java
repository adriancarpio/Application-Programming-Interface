package com.persona.cliente.service.impl;

import java.util.List;

import com.persona.cliente.exception.NewModelNotFoundException;
import com.persona.cliente.repo.IGenericRepo;
import com.persona.cliente.service.ICRUD;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID>{
	
	protected abstract IGenericRepo<T, ID> getRepo();

	@Override
	public T save(T t) {
		return getRepo().save(t);
	}

	@Override
	public T update(T t, ID id) {
		 getRepo().findById(id).orElseThrow(() -> new NewModelNotFoundException("ID NOT FOUND " + id));
	     return getRepo().save(t);
	}

	@Override
	public List<T> findAll() {
		return getRepo().findAll();
	}

	@Override
	public T findById(ID id) {
		return getRepo().findById(id).orElseThrow(() -> new NewModelNotFoundException("ID NOT FOUND " + id));
	}

	@Override
	public void delete(ID id) {
		getRepo().findById(id).orElseThrow(() -> new NewModelNotFoundException("ID NOT FOUND " + id));
        getRepo().deleteById(id);	
	}
}
