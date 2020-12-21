# Product Perspective

The DISEASE DETECTION ANDROID APPLICATION helps not only the patients but also the health-care professionals to predict the medical test results more effectively and in a bias-free manner by computerizing the prediction process.

Predictions or results will be generated from the medical test images which will help the patients to make appropriate decisions regarding the course of their treatment. For example, if the patient fails to get the doctor’s appointment, they can still know the results of their medical tests by simply using this android app and this in term saves time and money.

# Product Functionality


All the functions of the system will be performed in this order-

1. Select the disease
2. Upload image
3. Read image
4. Transform image
5. Evaluate image using the saved model
6. Determine and analyze the output
7. Display the output

# Product Design

The proposed idea was implemented using Java in the form of an android application having an interface and made using the Model-View-Controller (MVC) design pattern. The application is ”apk” installable. The Model and the View can run independently using the MVC pattern. The Model module can be used separately in other programs.


# The MVC Model

Model-view-controller (MVC) is an architectural pattern widely used to design user interfaces, which divides an application into three interrelated components. This is done to distinguish internal information representations from how information is communicated to and accepted from the user. The design pattern of MVC decouples these major components to allow efficient reuse of code and also allows parallel development of each of the component. This architecture is mostly used for desktop graphical user interfaces (GUIs) and is popular in designing web applications.


# The Model

A Model is the application's principal central component. It receives user inputs and commands via the View component and uses the logic to generate outputs, which is shown again via the View component. This Model comprises of various sub-models which represent each disease. In this project, we have used five different diseases, each having its own model.


i.MALARIA- model

For designing the malaria disease model, we have taken a dataset comprising of cell images of 50x50 pixels in .png format. The dataset is then divided into the train set and test set of 22046 and 5512 images respectively. Then Convolutional Neural Network (CNN) has been applied, having 3 convolutional layers and one fully-connected layer. ‘relu’ and ‘softmax’ activation functions are applied to the hidden layers and the output layer respectively. While compiling ‘categorical_crossentropy’ has been used as the loss function and ‘adam’ as an optimization function.

ii.PNEUMONIA- model

For pneumonia model, a dataset comprising of chest x-rays has been taken. The images are of 224x224 pixels. We then divided the dataset into the train set and test set and as the input images of both the sets are not balanced, we have used class-weight technique to balance it. To the dataset, CNN was applied, having 10 convolutional layers and one fully-connected layer. For activation purpose, ‘relu’ and ‘sigmoid’ functions are used in the hidden and output layer respectively. While compiling ‘binary_crossentropy’ has been used as the loss function and ‘adam’  as an optimization function.

iii.BREAST CANCER- model

For this, we have taken a dataset of cell images of micro-anatomy that contains RGB images of 50x50 pixels. To the dataset, we applied a CNN model of 6 convolutional layers and one fully-connected layer. To the output layer and the hidden layers, ‘sigmoid’ and ‘relu’ activation functions are applied. 

iv.	SKIN CANCER- model

We have taken a dataset comprising of RGB images of skin samples. The images were of 224x224 pixels. The dataset is then divided into the train set and test set of 2637 and 660 images respectively. We have then applied a CNN model of 4 convolutional layers and one fully-connected layer. For activation,  ‘softmax’ and ‘relu’ are applied to the output layer and hidden layers respectively. While compiling, ‘categorical_crossentropy’ has been used as loss function and ‘adam’ as an optimization function.

v.	COVID 19- model

As it was a completely new disease and there is very little information about this disease, so a wide data set for the disease was likely to be found. However, an image dataset of Human Lung CT Scans having 349 lung CT scan images of Covid positive patients and 397 lung CT scan images of Covid negative patients was obtained from Kaggle. This model was trained using Google’s Teachable Machine in 75 epochs with a bath size of 128 images and 0.001 learning rate.

# The View

A View is something available to the user. It reflects the user interface with which the user is communicating while using an application. While the View has buttons, it, itself remains unaware of the fundamental interaction that exists with the back-end. It helps UI / UX people to operate in parallel with the people at the back-end of the user interface.

# The Controller

A Controller is a master that synchronizes the Model along with View. It obtains the user's interaction with the View, transmits them on to the Model that then processes the input information for output production. Through the View, the outputs (results) are then shown to the user.

# How 'MVC' fits into this project

The View in our project is represented by the android application interface where the user can select the disease, upload the image, and generate the output. The results will be displayed to the user through this interface only. When the user will press the detect button available on the screen, a function will be called at the backend which in turn will call other functions each having the unique functionality i.e., transforming the image, augmenting it, importing the model, interpreting the output etc. The function calls will work as controller in our project. The custom CNN models are present in the firebase repository and also in the assets folder of the android application, which are imported by the controller at the time function calls. With the help of these, we can alter and run the GUI component separately without compromising the functionality of the other components. Similarly, by importing separately from the GUI we can modify the Detector component and use it as a module as well.

# Working

On opening "DISEASE DETECION APP" application, the main activity displays five buttons (for each disease). When the user clicks on the button of a particular disease, a new activity will be launched through the onClick() method of the respective button. This new activity consists of the Float Action Button, the ‘Detect Image’ button along with an ImageView. The Float Action Button calls upon the onImageFromGalleryClick() function which reads the path of the image that is to be uploaded using Intent.ACTION_GET_CONTENT. The uploaded image is then resized into 224 x 224 pixels automatically before displaying it to the user at the ImageView with the help of the setImageURI() function. 

When the user clicks on the ‘Detect Image’ button, the button calls upon the detect() function which then retrieves the path of the uploaded image from the imageView and takes it as an input using getYourInputImage() function. The image is then transformed into the target size before loading. The corresponding saved CNN model is then used to classify the image. The result is displayed to the user on the screen through the toast. In this way, the user can classify different images of a particular disease in the same activity or can go back to the menu to detect different diseases.

# Installation

The application is “apk” installable and can be installed in the following way-

1. Click on the package named “diseasedetction.apk”, and then press the Install button.

2. You will see the application package installing and after it is done press Close.

3. Open the “Disease Detection App” to run the application.
