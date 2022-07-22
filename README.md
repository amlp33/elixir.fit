# elixir.fit

---

https://user-images.githubusercontent.com/68852345/180461811-8901cf2f-5b3e-4926-93bd-5cd1f12ec4f5.mp4

Sorry for bad quality ( as github doesn't allow a video size > 10mb
---


## A project funded under the innovation program by MITWPU.
Previously it was supposed to be a yoga based android application
for blind people, so they can perform yoga without an instructor.
But the scope was increased to handicapped people as well.

## ** presently the prototype has necessary features including
      - User Registration
      - User authentication
      - profile page
      - calorie/BMI/BMR calculator
      - profile updation
      - sample cardio/yoga videos
      - feedback form
      - session registration
      
## Features to be added or trimmed
      - Presently the youtube's player API is implemented
        which is not a viable option as it only provides
        10K video access a day and does not include a pay
        plan for large scale. So it has to be replaced with
        a different video streaming platform for fluent streaming
        experience and that can scale accordinly.
       
      - customized diet
      - community Q n A ( A tab where people can ask questions and can 
        get answers from other users or verified trainers/dietitians.
        Helping them to form community and get the most out of the application
        
      - More workout/Wellness options like theraphy / warmups / stretching /
        workout-of-the-day
      
      - Gamification ( to help users reach there goals and get some rewards 
        Like free diet plan or training sessions or some other unique rewards ) 
        
      
   ## Bugs 
_A project without bugs is not a project_
       
       - As Cloud firestore is used as entire backend, so the cost is based on the
         read & writes to the database. But the code is not optimised well to decrease
         the read/writes. For Eg: The user data when he/she is registered can be stored 
         locally and fetched through-out all the acitivites but instead its being fetched
         in each activity like while calculating BMI/BMR etc.
         
       - Theme for dark mode is not implemented as yet.
       
       - Voice input on login page ( its pause period is very less like 1 - 2 sec ) 
         if users pauses for 1 - 2 sec max the voice input closes and user have to input
         again, which is a headache. ( couldn't find a right solution for this.
       
       - A small bug for resend OTP ( not yet clear when the BUG occurs ) 
       
       - A weird bug regarding the filtering for blind / handicapped users
         Eg : The exercise-videos for each users are different depending on the disability
              But presently because of not well written collections on firestore the
              filtering has became a bit pain in the head.
              
        
         
                      
      
