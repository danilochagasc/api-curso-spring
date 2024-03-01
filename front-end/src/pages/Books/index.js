import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';

import './styles.css';

import logoImage from '../../assets/logo.svg';
import api from "../../services/api";

export default function Books() {
  const [books, setBooks] = useState([]);
  const [page, setPage] = useState(1);

  const username = localStorage.getItem('username');
  const accessToken = localStorage.getItem('accessToken');

  const navigate = useNavigate();

  const header = {
    headers: {
      'Authorization': `Bearer ${accessToken}`
    }
  };

  const param = {
    params: {
      page: 1,
      size: 4,
      direction: 'asc',
    }
  };

  async function logout() {
    localStorage.clear();
    navigate('/');
  }

  async function editBook(id) {
    try {
      navigate(`/book/new/${id}`);
    } catch (error) {
        alert('Error editing book, try again.');
    }

  }

  async function deleteBook(id) {
    try {
      await api.delete(`api/book/v1/${id}`, header);

      setBooks(books.filter(book => book.id !== id));
    } catch (error) {
      alert('Error deleting book, try again.');
    }
  }

  async function fetchMoreBooks() {
        try {
            const response = await api.get('api/book/v1', {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                },
                params: {
                    page: page,
                    size: 4,
                    direction: 'asc'
                }
            });

            const newBooks = response.data._embedded?.bookVOes || [];
            setBooks(prevBooks => [...prevBooks, ...newBooks]);
            setPage(page + 1);
        } catch (error) {
            alert('Error fetching books, try again.');
        }
    }


    useEffect(() => {
        fetchMoreBooks();
    }, [])
  

  return (
    <div className="book-container">
      <header>
        <img src={logoImage} alt="Erudio" />
        <span>Welcome, <strong>{username.toUpperCase()}</strong>!</span>
        <Link className="button" to="/book/new/0">Add New Book</Link>

        <button onClick={logout} type="button">
          <FiPower size={18} color="#251FC5" />
        </button>
      </header>

      <h1>Registered Books</h1>
      <ul>
        {books && books.map(book => (
          <li key={book.id}>
            <strong>Title:</strong>
            <p>{book.title}</p>

            <strong>Author:</strong>
            <p>{book.author}</p>

            <strong>Price:</strong>
            <p>{Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(book.price)}</p>

            <strong>Release Date:</strong>
            <p>{Intl.DateTimeFormat('pt-BR').format(new Date(book.launchDate))}</p>

            <button onClick={() => editBook(book.id)} type="button">
              <FiEdit size={20} color="#251FC5" />
            </button>

            <button onClick={() => deleteBook(book.id)} type="button">
              <FiTrash2 size={20} color="#251FC5" />
            </button>
          </li>
        ))}
      </ul>

      <button onClick={fetchMoreBooks} className="button" type="button">Load More</button>
    </div>
  );
}
