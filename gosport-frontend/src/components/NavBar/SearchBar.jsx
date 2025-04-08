import React, { useState, useEffect, useRef } from 'react';
import { FaSearch, FaTimes } from 'react-icons/fa';
import { fetchSearchResults } from '../../services/searchService';
import { useNavigate } from 'react-router-dom';

const SearchBar = () => {
    const [query, setQuery] = useState('');
    const [results, setResults] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isFocused, setIsFocused] = useState(false);
    const searchRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (searchRef.current && !searchRef.current.contains(e.target)) {
                setIsFocused(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    useEffect(() => {
        const debounceTimer = setTimeout(() => {
            if (query.trim() && isFocused) {
                fetchResults(query);
            } else {
                setResults([]);
            }
        }, 10);

        return () => clearTimeout(debounceTimer);
    }, [query, isFocused]);

    const fetchResults = async (searchQuery) => {
        setIsLoading(true);
        try {
            const data = await fetchSearchResults(searchQuery);
            setResults(data);
        } catch (error) {
            console.error('Błąd wyszukiwania:', error);
            setResults([]);
        }
        setIsLoading(false);
    };

    const handleClear = () => {
        setQuery('');
        setResults([]);
    };

    return (
        <div className="relative hidden md:block ml-6" ref={searchRef}>
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <FaSearch className="text-gray-400" />
            </div>
            <input
                type="text"
                placeholder="Wyszukaj użytkowników..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                onFocus={() => setIsFocused(true)}
                className="block w-full pl-10 pr-10 py-2 border border-gray-300 rounded-2xl leading-5 bg-gray-50 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            />
            
            {query && (
                <button
                    onClick={handleClear}
                    className="absolute inset-y-0 right-0 pr-3 flex items-center"
                >
                    <FaTimes className="text-gray-400 hover:text-gray-600" />
                </button>
            )}

            {isFocused && (query || results.length > 0) && (
                <div className="absolute top-full left-0 w-full mt-1 bg-white border border-gray-200 rounded-lg shadow-lg z-50 max-h-96 overflow-y-auto">
                    {isLoading ? (
                        <div className="p-3 text-center text-gray-500">Loading...</div>
                    ) : results.length > 0 ? (
                        results.map((user, index) => (
                            <div 
                                key={`${user.userId}-${index}`} 
                                className="p-3 hover:bg-gray-50 cursor-pointer flex items-center border-b border-gray-100 last:border-0"
                                onClick={() => {
                                    navigate(`/dashboard/profile/${user.userId}`);
                                    setIsFocused(false);
                                }}
                            >
                                <img 
                                    src={user.profileImage || '/default-avatar.png'} 
                                    alt={`${user.firstName} ${user.lastName}`}
                                    className="w-8 h-8 rounded-full mr-3 object-cover"
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = '/default-avatar.png';
                                    }}
                                />
                                <div>
                                    <p className="font-medium text-gray-900">
                                        {user.firstName} {user.lastName}
                                    </p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="p-3 text-center text-gray-500">
                            {query ? "Brak wyników" : "Wpisz zapytanie..."}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchBar;