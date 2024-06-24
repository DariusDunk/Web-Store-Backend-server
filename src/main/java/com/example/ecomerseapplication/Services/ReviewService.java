package com.example.ecomerseapplication.Services;

import com.example.ecomerseapplication.DTOs.ReviewRequest;
import com.example.ecomerseapplication.Entities.Customer;
import com.example.ecomerseapplication.Entities.Product;
import com.example.ecomerseapplication.Entities.Review;
import com.example.ecomerseapplication.Repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    public void save(Review review) {
        reviewRepository.save(review);
    }

    @Transactional
    public void update(Review review) {
        reviewRepository.updateReview(review.getId(), review.getRating(), review.getReviewText());
    }

    public boolean exists(Product product, Customer customer) {
        return reviewRepository.existsByProductAndCustomer(product, customer);
    }

    public Review getByProdCust(Product product, Customer customer) {
        return reviewRepository.getByProductAndCustomer(product, customer).orElse(null);
    }

    @Transactional
    public Product manageReview(Product product, Customer customer, ReviewRequest request) {

        Review existingReview = getByProdCust(product, customer);

        if (existingReview != null) {
            return updateReview(existingReview, request, product);
        }
        return createReview(product, customer, request);


    }

    @Transactional
    public Product createReview(Product product, Customer customer, ReviewRequest request) {

        short adjustedRating = (short) (request.rating * 10);
        Review review = new Review();

        review.setProduct(product);
        review.setCustomer(customer);
        review.setReviewText(request.reviewText);
        review.setRating(adjustedRating);

        if (product.getReviews().isEmpty())
            product.setRating(request.rating);

        else {
            short rating = (short) (((product.getRating() * product.getReviews().size()) + adjustedRating) / (product.getReviews().size() + 1));
            product.setRating(rating);
        }

        save(review);

        return product;
    }

    @Transactional
    public Product updateReview(Review existingReview, ReviewRequest request, Product product) {
        short adjustedRating = (short) (request.rating * 10);

        if (existingReview.getRating() == request.rating
                && existingReview.getReviewText().equals(request.reviewText))
            return null;

        existingReview.setRating(adjustedRating);
        existingReview.setReviewText(request.reviewText);
        update(existingReview);

        if (product.getReviews().size() == 1)
            product.setRating(request.rating);

        else {
            short oldRating = 0;
            for (Review review : product.getReviews()) {
                oldRating += review.getRating();
            }

            short newRating = (short) (((oldRating - existingReview.getRating()) + adjustedRating) / product.getReviews().size());


            product.setRating(newRating);
        }

        return product;
    }
}



