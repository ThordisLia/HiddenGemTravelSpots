package com.hiddengems.hiddengems.controllers;


import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.Review;
import com.hiddengems.hiddengems.models.User;
import com.hiddengems.hiddengems.models.data.ReviewRepository;
import com.hiddengems.hiddengems.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);

        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    @GetMapping("/reviews/add")
    public String displayReviewForm(HttpServletRequest request, Model model) {
        model.addAttribute("title", "Review a Gem");
        model.addAttribute(new Review());
        return "reviews/add";
    }

    @PostMapping("/reviews/add")
    public String processReviewForm(@ModelAttribute @Valid Review newReview,
                                    Errors errors, HttpServletRequest request, Model model) {

        User user = getUserFromSession(request.getSession());

        if (errors.hasErrors()) {
            return "reviews/add.html";
        } else {
            newReview.setUser(user);
            reviewRepository.save(newReview);
        }

        return "redirect:../";
    }

    @GetMapping("/reviews/edit/{reviewId}")
    public String displayEditReviewForm(Model model, @PathVariable int reviewId) {

        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isPresent()) {
            Review oldReview = (Review) review.get();
            model.addAttribute("review", oldReview);
            model.addAttribute("edit", true);
            return "reviews/edit.html";
        } else {
            return "redirect:../";
        }
    }

    @PostMapping("/reviews/edit/{reviewId}")
    public String processEditReviewForm(@PathVariable Integer reviewId, @ModelAttribute @Valid Review updatedReview, Error errors,
                                        HttpServletRequest request, Model model) {


        Optional<Review> review = reviewRepository.findById(reviewId);



        if (review.isPresent()) {
            Review newReview = (Review) review.get();
            User user = getUserFromSession(request.getSession());

            if (newReview.getUser().getId() == user.getId()) {
                newReview.setUser(user);
                newReview.setReviewText(updatedReview.getReviewText());
                newReview.setThumbsup(updatedReview.getThumbsup());
                reviewRepository.save(newReview);
                return "success-test";
            } else {
                // TODO: redirect to error page (create error page)
            }
        }

        return "redirect:../";
    }

    @PostMapping("/delete/")
    public String processDeleteReview(@ModelAttribute Review review, HttpServletRequest request, Model model) {
//        Optional<Review> review2 = reviewRepository.findById(review.getId());
//        Review newReview = (Review) review2.get();
//        model.addAttribute("message", "This will delete the review eventually. Review ID = " + newReview.getId());
        model.addAttribute("message", "This will delete the review eventually.");
        return "success-test";
    }

    // TODO: Update the Review template with conditional statements to populate the fields with pre-existing review data & flip the Submit button to Save
    // TODO: Add 'Delete' handlers

}
