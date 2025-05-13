package com.draker.recmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.draker.recmaster.R;
import com.draker.recmaster.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для отображения списка книг в RecyclerView
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private final List<Book> books;
    private final OnBookClickListener listener;

    /**
     * Интерфейс для обработки событий клика на книгу
     */
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    /**
     * Конструктор адаптера
     * @param context контекст активности
     * @param listener слушатель кликов
     */
    public BookAdapter(Context context, OnBookClickListener listener) {
        this.context = context;
        this.books = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    /**
     * Обновляет список книг
     * @param newBooks новый список книг
     */
    public void setBooks(List<Book> newBooks) {
        this.books.clear();
        if (newBooks != null) {
            this.books.addAll(newBooks);
        }
        notifyDataSetChanged();
    }

    /**
     * ViewHolder для книги
     */
    class BookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView coverImageView;
        private final TextView titleTextView;
        private final TextView authorsTextView;
        private final TextView categoriesTextView;
        private final TextView publisherTextView;
        private final TextView ratingTextView;
        private final TextView pageCountTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImageView = itemView.findViewById(R.id.iv_book_cover);
            titleTextView = itemView.findViewById(R.id.tv_book_title);
            authorsTextView = itemView.findViewById(R.id.tv_book_authors);
            categoriesTextView = itemView.findViewById(R.id.tv_book_categories);
            publisherTextView = itemView.findViewById(R.id.tv_book_publisher);
            ratingTextView = itemView.findViewById(R.id.tv_book_rating);
            pageCountTextView = itemView.findViewById(R.id.tv_book_page_count);
        }

        public void bind(final Book book, final OnBookClickListener listener) {
            titleTextView.setText(book.getTitle());
            
            // Загружаем обложку с помощью Glide
            if (book.getCoverUrl() != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                
                Glide.with(context)
                        .load(book.getCoverUrl())
                        .apply(requestOptions)
                        .placeholder(R.drawable.placeholder_book)
                        .error(R.drawable.placeholder_book)
                        .into(coverImageView);
            } else {
                coverImageView.setImageResource(R.drawable.placeholder_book);
            }

            // Отображаем авторов
            String authors = book.getAuthorsString();
            if (!authors.isEmpty()) {
                authorsTextView.setVisibility(View.VISIBLE);
                authorsTextView.setText(authors);
            } else {
                authorsTextView.setVisibility(View.GONE);
            }

            // Отображаем категории
            String categories = book.getCategoriesString();
            if (!categories.isEmpty()) {
                categoriesTextView.setVisibility(View.VISIBLE);
                categoriesTextView.setText(categories);
            } else {
                categoriesTextView.setVisibility(View.GONE);
            }

            // Издатель и дата публикации
            if (book.getPublisher() != null && !book.getPublisher().isEmpty()) {
                String publisherInfo = book.getPublisher();
                if (book.getPublishedDate() != null && !book.getPublishedDate().isEmpty()) {
                    publisherInfo += ", " + book.getPublishedDate();
                }
                publisherTextView.setVisibility(View.VISIBLE);
                publisherTextView.setText(publisherInfo);
            } else if (book.getPublishedDate() != null && !book.getPublishedDate().isEmpty()) {
                publisherTextView.setVisibility(View.VISIBLE);
                publisherTextView.setText(book.getPublishedDate());
            } else {
                publisherTextView.setVisibility(View.GONE);
            }

            // Рейтинг
            if (book.getAverageRating() > 0) {
                ratingTextView.setVisibility(View.VISIBLE);
                ratingTextView.setText(context.getString(R.string.rating_format, book.getAverageRating()));
            } else {
                ratingTextView.setVisibility(View.GONE);
            }

            // Количество страниц
            if (book.getPageCount() > 0) {
                pageCountTextView.setVisibility(View.VISIBLE);
                pageCountTextView.setText(context.getString(R.string.page_count_format, book.getPageCount()));
            } else {
                pageCountTextView.setVisibility(View.GONE);
            }

            // Обработка клика на элемент
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });
        }
    }
}