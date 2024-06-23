package com.example.ecomerseapplication.EntityToDTOConverters;

import com.example.ecomerseapplication.DTOs.ReviewResponse;
import com.example.ecomerseapplication.Entities.Review;

public class ReviewEntToDTO {

    public static ReviewResponse entityToResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.reviewText = review.getReviewText();
        reviewResponse.rating = review.getRating();
        reviewResponse.customerDetailsForReview.customerPfp = review.getCustomer().getCustomerPfp();
        reviewResponse.customerDetailsForReview.name = review.getCustomer().getName();

        return reviewResponse;

    }


}
