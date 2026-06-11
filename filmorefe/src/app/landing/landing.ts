import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: false,
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {
  landingForm!: FormGroup;
  year = new Date().getFullYear();

  constructor(
    private fb: FormBuilder,
    private router: Router,
  ) {
    this.landingForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  login() {
    this.router.navigate(['/login']);
  }

  getStarted() {
    this.router.navigate(['/signup'], {
      queryParams: { email: this.landingForm.value.email },
    });
  }

  reasons = [
    {
      title: 'Enjoy on your TV',
      text: 'Watch on Smart TVs, Playstation, and more.',
      icon: 'tv',
    },
    {
      title: 'Download and watch offline',
      text: 'Save your favorites easily and always have something to watch.',
      icon: 'file_download',
    },
    {
      title: 'Watch everywhere',
      text: 'Stream unlimited movies and TV shows on your phone, tablet, laptop, and TV.',
      icon: 'devices',
    },
    {
      title: 'Create profiles for kids',
      text: 'Send kids on adventures with their favorite characters in a space made just for them—free with your membership.',
      icon: 'child_care',
    },
  ];

  faqs = [
    {
      question: 'What is Filmore?',
      answer:
        'Filemore is a streaming service that offers a wide variety of award-winning TV shows, movies, anime, documentaries, and more on thousands of internet-connected devices. You can watch as much as you want, whenever you want without a single commercial ad, all for one low monthly price.!',
    },
    {
      question: 'How much does Filmore cost?',
      answer:
        'For only 100$ per month, watch Filmore on your phone, tablet, Smart TV, laptop, or streaming device, all for one fixed monthly fee. No extra costs, no contracts.',
    },
    {
      question: 'How do I cancel?',
      answer:
        'Filmore is flexible. There are no annoying contracts and no commitments. You can easily cancel your account online in two clicks. There are no cancellation fees – start or stop your account anytime.',
    },
    {
      question: 'What can I watch on Filmore?',
      answer:
        'Filmore has an extensive library of feature films, documentaries, TV shows, anime, award-winning Netflix originals, and more. Watch as much as you want, anytime you want.',
    },
    {
      question: 'Is Filmore good for kids?',
      answer:
        "The Filmore Kids experience is included in your membership to give parents control while kids enjoy family-friendly TV shows and movies in their own space. Kids profiles come with PIN-protected parental controls that let you restrict the maturity rating of content kids can watch and block specific titles you don't want kids to see.",
    },
  ];
}
