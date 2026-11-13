package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Data model representing an Indian Language in the "India All Languages Yard".
 * Supports 14 major official and classical languages of India with native script,
 * greetings, state associations, and theme accent colors.
 */
data class IndianLanguage(
  val code: String,
  val nativeName: String,
  val englishName: String,
  val region: String,
  val kannadaRegion: String,
  val greeting: String,
  val description: String,
  val kannadaDescription: String,
  val sampleHeadline: String,
  val accentColor: Color,
  val popularBadge: String? = null
)

object IndianLanguagesRepository {
  val allLanguages: List<IndianLanguage> = listOf(
    IndianLanguage(
      code = "kn",
      nativeName = "ಕನ್ನಡ",
      englishName = "Kannada",
      region = "Karnataka",
      kannadaRegion = "ಕರ್ನಾಟಕ",
      greeting = "ನಮಸ್ಕಾರ!",
      description = "ಕರ್ನಾಟಕದ ಹೆಮ್ಮೆಯ ಅಧಿಕೃತ ಭಾಷೆ ಮತ್ತು ಶಾಸ್ತ್ರೀಯ ಸಾಹಿತ್ಯ",
      kannadaDescription = "ರಾಜ್ಯದ ಅಧಿಕೃತ ಭಾಷೆ · ಸಾಹಿತ್ಯ, ಕಲೆ ಮತ್ತು ಜ್ಞಾನದ ತಾಣ",
      sampleHeadline = "ಕರುನಾಡ ಯುವ ಸಮೂಹದ ನಂ.1 ವೀಡಿಯೊ ವೇದಿಕೆ",
      accentColor = Color(0xFFE50914),
      popularBadge = "PRIMARY"
    ),
    IndianLanguage(
      code = "hi",
      nativeName = "हिन्दी",
      englishName = "Hindi",
      region = "Pan-India / North & Central",
      kannadaRegion = "ಭಾರತದಾದ್ಯಂತ",
      greeting = "नमस्ते!",
      description = "भारत की आधिकारिक भाषा और जन-जन का संपर्क माध्यम",
      kannadaDescription = "ರಾಷ್ಟ್ರೀಯ ಸಂಪರ್ಕ ಭಾಷೆ ಮತ್ತು ಮನರಂಜನೆ",
      sampleHeadline = "मनोरंजन, ज्ञान और भारत की आवाज",
      accentColor = Color(0xFFFF6D00),
      popularBadge = "POPULAR"
    ),
    IndianLanguage(
      code = "en",
      nativeName = "English",
      englishName = "English (India)",
      region = "Pan-India & Global",
      kannadaRegion = "ಜಾಗತಿಕ ಮತ್ತು ಭಾರತ",
      greeting = "Welcome!",
      description = "Global connect, modern education and youth creators",
      kannadaDescription = "ಜಾಗತಿಕ ಶಿಕ್ಷಣ ಮತ್ತು ತಂತ್ರಜ್ಞಾನ",
      sampleHeadline = "Empowering Indian Youth Creators Globally",
      accentColor = Color(0xFF0066FF),
      popularBadge = "GLOBAL"
    ),
    IndianLanguage(
      code = "te",
      nativeName = "తెలుగు",
      englishName = "Telugu",
      region = "Andhra Pradesh & Telangana",
      kannadaRegion = "ಆಂಧ್ರ ಮತ್ತು ತೆಲಂಗಾಣ",
      greeting = "నమస్కారం!",
      description = "తేనెలొలుకు తెలుగు - ఇటాలియన్ ఆఫ్ ది ఈస్ట్",
      kannadaDescription = "ಆಂಧ್ರ ಮತ್ತು ತೆಲಂಗಾಣದ ಮಾಧುರ್ಯ ಭಾಷೆ",
      sampleHeadline = "తెలుగు చలనచిత్రాలు, విజ్ఞానం మరియు సంగీతం",
      accentColor = Color(0xFF00897B),
      popularBadge = "TRENDING"
    ),
    IndianLanguage(
      code = "ta",
      nativeName = "தமிழ்",
      englishName = "Tamil",
      region = "Tamil Nadu & Puducherry",
      kannadaRegion = "ತಮಿಳುನಾಡು",
      greeting = "வணக்கம்!",
      description = "உலகிலேயே மிகவும் தொன்மையான செம்மொழி",
      kannadaDescription = "ಪುರಾತನ ಶಾಸ್ತ್ರೀಯ ಸಾಹಿತ್ಯದ ಸವಿನೆಲೆ",
      sampleHeadline = "இலக்கியம், கலை, ஆன்மீகம் மற்றும் இளைஞர் களம்",
      accentColor = Color(0xFFD81B60),
      popularBadge = "CLASSICAL"
    ),
    IndianLanguage(
      code = "ml",
      nativeName = "മലയാളം",
      englishName = "Malayalam",
      region = "Kerala & Lakshadweep",
      kannadaRegion = "ಕೇರಳ",
      greeting = "നമസ്കാരം!",
      description = "ദൈവത്തിന്റെ സ്വന്തം നാടിന്റെ മനോഹര ഭാഷ",
      kannadaDescription = "ಕೇರಳದ ಸೌಂದರ್ಯ ಮತ್ತು ಸಾಂಸ್ಕೃತಿಕ ಭಾಷೆ",
      sampleHeadline = "കല, പ്രകൃതി, സിനിമ, സാങ്കേതിക വിദ്യ",
      accentColor = Color(0xFF2E7D32)
    ),
    IndianLanguage(
      code = "mr",
      nativeName = "मराठी",
      englishName = "Marathi",
      region = "Maharashtra & Goa",
      kannadaRegion = "ಮಹಾರಾಷ್ಟ್ರ",
      greeting = "नमस्कार!",
      description = "छत्रपती शिवाजी महाराजांची स्वाभिमानी मायमराठी",
      kannadaDescription = "ಶಿವಾಜಿ ಮಹಾರಾಜರ ನಾಡಿನ ಹೆಮ್ಮೆಯ ಭಾಷೆ",
      sampleHeadline = "इतिहास, संस्कृती आणि नवयुवकांची आवड",
      accentColor = Color(0xFFF4511E)
    ),
    IndianLanguage(
      code = "bn",
      nativeName = "বাংলা",
      englishName = "Bengali",
      region = "West Bengal & Tripura",
      kannadaRegion = "ಪಶ್ಚಿಮ ಬಂಗಾಳ",
      greeting = "নমস্কার!",
      description = "রবীন্দ্রনাথ ଓ নেতাজির মিষ্টি বাংলা ভাষা",
      kannadaDescription = "ರವೀಂದ್ರನಾಥ ಟ್ಯಾಗೋರ್ ಅವರ ಸಾಹಿತ್ಯಿಕ ಭಾಷೆ",
      sampleHeadline = "সাহিত্য, সুর, শিল্পকলা এবং যুবচেতনা",
      accentColor = Color(0xFF8E24AA)
    ),
    IndianLanguage(
      code = "gu",
      nativeName = "ગુજરાતી",
      englishName = "Gujarati",
      region = "Gujarat & Daman",
      kannadaRegion = "ಗುಜರಾತ್",
      greeting = "નમસ્તે / કેમ છો!",
      description = "ગરવી ગુજરાત અને વ્યાપાર જગતની ધબકાર",
      kannadaDescription = "ಉದ್ಯಮಶೀಲತೆ ಮತ್ತು ಸಂಸ್ಕೃತಿಯ ತವರು",
      sampleHeadline = "ઉદ્યોગ, પ્રવાસ, વાર્તાઓ અને લોકસંસ્કૃતિ",
      accentColor = Color(0xFFF57C00)
    ),
    IndianLanguage(
      code = "pa",
      nativeName = "ਪੰਜਾਬੀ",
      englishName = "Punjabi",
      region = "Punjab & Chandigarh",
      kannadaRegion = "ಪಂಜಾಬ್",
      greeting = "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ!",
      description = "ਜੋਸ਼, ਭੰਗੜਾ ਅਤੇ ਪੰਜ ਦਰਿਆਵਾਂ ਦੀ ਮਿੱਠੀ ਬੋਲੀ",
      kannadaDescription = "ಉತ್ಸಾಹ, ಸಂಗೀತ ಮತ್ತು ಶೌರ್ಯದ ಭಾಷೆ",
      sampleHeadline = "ਸੰਗੀਤ, ਉਤਸਾਹ, ਖੇਡਾਂ ਅਤੇ ਦਿਲਦਾਰ ਪੰਜਾਬ",
      accentColor = Color(0xFF1E88E5)
    ),
    IndianLanguage(
      code = "or",
      nativeName = "ଓଡ଼ିଆ",
      englishName = "Odia",
      region = "Odisha",
      kannadaRegion = "ಒಡಿಶಾ",
      greeting = "ନମସ୍କାର!",
      description = "ଜଗନ୍ନାଥ ଧାମ ଏବଂ କଳିଙ୍ಗର ଶାସ୍ତ୍ରୀୟ ଭାಷା",
      kannadaDescription = "ಜಗನ್ನಾಥ ಕ್ಷೇತ್ರ ಮತ್ತು ಕಳಿಂಗದ ಸಂಸ್ಕೃತಿ",
      sampleHeadline = "ପରମ୍ପରା, ନୃତ୍ୟ, ସାହିତ୍ୟ ଏବଂ ସିନେମା",
      accentColor = Color(0xFF00ACC1)
    ),
    IndianLanguage(
      code = "as",
      nativeName = "অসমীয়া",
      englishName = "Assamese",
      region = "Assam & Northeast",
      kannadaRegion = "ಅಸ್ಸಾಂ ಮತ್ತು ಈಶಾನ್ಯ ಭಾರತ",
      greeting = "নমস্কাৰ!",
      description = "ব্ৰহ্মপুত্ৰ উপত্যকাৰ সোণালী সংস্কৃতিৰ ভাষা",
      kannadaDescription = "ಬ್ರಹ್ಮಪುತ್ರ ಕಣಿವೆಯ ನಿಸರ್ಗ ಮತ್ತು ಸಂಸ್ಕೃತಿ",
      sampleHeadline = "বিহু, প্ৰকೃতি, সংস্কৃতি আৰু নৱপ্ৰজন্ম",
      accentColor = Color(0xFF43A047)
    ),
    IndianLanguage(
      code = "ur",
      nativeName = "اردو",
      englishName = "Urdu",
      region = "Pan-India",
      kannadaRegion = "ಭಾರತದಾದ್ಯಂತ",
      greeting = "آداب / السلام علیکم!",
      description = "شاعری، تہذیب اور نزاکت کی خوبصورت زبان",
      kannadaDescription = "ಶಾಯರಿ, ಕಾವ್ಯ ಮತ್ತು ಸೌಂದರ್ಯದ ಭಾಷೆ",
      sampleHeadline = "غزل، شاعری، علم اور ادبی شاہکار",
      accentColor = Color(0xFF00897B)
    ),
    IndianLanguage(
      code = "sa",
      nativeName = "संस्कृतम्",
      englishName = "Sanskrit",
      region = "Pan-India Classical Heritage",
      kannadaRegion = "ಭಾರತೀಯ ಸನಾತನ ಪರಂಪರೆ",
      greeting = "नमो नमः!",
      description = "सर्वासां भारतीयभाषाणां जननी देववाणी",
      kannadaDescription = "ಭಾರತೀಯ ಎಲ್ಲಾ ಭಾಷೆಗಳ ತಾಯಿ ಮತ್ತು ಜ್ಞಾನ ಭಂಡಾರ",
      sampleHeadline = "ವೇದಾಃ, ಉಪನಿಷದಃ, ಯೋಗಃ ತಥಾ ಆಧ್ಯಾತ್ಮಮ್",
      accentColor = Color(0xFF6A1B9A),
      popularBadge = "HERITAGE"
    )
  )

  fun findByName(name: String): IndianLanguage? {
    return allLanguages.find {
      it.nativeName.equals(name, ignoreCase = true) ||
          it.englishName.equals(name, ignoreCase = true) ||
          it.code.equals(name, ignoreCase = true)
    }
  }
}
