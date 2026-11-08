# Tip Hub — 5 Visual Styles Multi-App Project

This repository contains the complete implementation of the **Tip Hub** mobile app built in **5 distinct visual styles**, each completely self-contained in its own independent directory.

---

## 🎨 Visual Style Comparison

| Style | Folder | Aesthetic & Palette | Home Order | App Tile Shape & Icon | Row Thumbnails | Featured Layout | Detail Hero | Motion Signature |
|---|---|---|---|---|---|---|---|---|
| **1. Editorial Glass** | `style-1-editorial-glass/` | Warm paper & plum (`#FAF3E6`, `#1E1A24`, `#8A2B4D`) | Tip first | Rounded squares (r22), white stroke | Left | Image above text | Inset (190px, r22) | 3 drifting blurred blobs (9–13s) |
| **2. Neon Newsroom** | `style-2-neon-newsroom/` | Dark cyber neon (`#0B0A24`, `#5CFFE0`, `#FF3DAA`) | Apps first | Circles, dark stroke (`#0B0A24`) | Right | Overlaid frosted caption panel | Full-bleed (r36 bottom) | 3 glowing aurora ribbons (6–8s) |
| **3. Sticker Brutal** | `style-3-sticker-brutal/` | Neo-brutalist pop (`#FFE45C`, `#1A1A1A`, 3px borders) | Tip first | Rounded squares (r18), 3px border, 5px shadow | Left | Image above text with 3px border | Inset (3px border, 5px shadow) | Hard solid geometric disks, ±5° tile wobble |
| **4. Ocean Magazine** | `style-4-ocean-magazine/` | Deep oceanic glass (`#0A3B52`, `#2DD4BF`, `#FB7185`) | Apps first | Circles with subtle stroke | Left | Overlaid frosted caption panel | Full-bleed (r36 bottom) | Rising translucent bubbles (8–12s) |
| **5. Cosmic Soft** | `style-5-cosmic-soft/` | Dreamy night-sky (`#12082E`, `#FFD166`, `#C9BFF5`) | Tip first | Rounded squares (r26, roundest), white stroke | Right | Image above text | Inset (r28) | 8 twinkling stars + purple nebula aura |

---

## 📱 Screens Implemented per Style

1. **Home Screen:**
   - Greeting ("Hi there!"), user name ("Amina Bekele"), pulsing profile avatar.
   - "Tip of the day" card with continuous shine sweep animation and "Try it now" button.
   - "Your apps" 3x2 grid featuring 6 apps: Notes, Timer, Draw, Music, Learn, Games (using custom SVG stroke icons, never emoji).
   - Two "Quick tip" glass cards ("Drink a glass of water", "Sleep before ten").
   - Floating glass bottom navigation bar.

2. **Article List Screen:**
   - Header ("Today's reads") with accessible search icon button (min 44x44).
   - Filter chips: All, Tech, Health, Learn.
   - 1 Featured story card ("Five small habits that make mornings calmer").
   - 3 Story rows:
     - `TECH · 5 min`: "Chipmakers race to put AI on every phone"
     - `HEALTH · 4 min`: "Weekend trails worth the early start"
     - `LEARN · 3 min`: "Why short breaks help you study longer"
   - Floating glass bottom navigation bar.

3. **Article Detail Screen:**
   - Top navigation bar: Back button, Share button, Save button.
   - Dynamic Hero image banner (inset or full-bleed according to style spec).
   - Category tag & read time ("LIFESTYLE · 4 MIN") + Article headline.
   - Author row ("Amina Bekele · Nov 8") with avatar.
   - Two complete article body paragraphs.
   - Dedicated "Quick tip" glass card ("Set a timer for just two minutes of stretching").
   - Floating glass action bar with Like, Comment, and primary Listen buttons.

---

## 🚀 How to Run Any Style

Each style is an independent Expo / React Native application. Navigate to any style directory and start:

```bash
# To run Style 1: Editorial Glass
cd style-1-editorial-glass
npm install
npx expo start

# To run Style 2: Neon Newsroom
cd style-2-neon-newsroom
npm install
npx expo start

# To run Style 3: Sticker Brutal
cd style-3-sticker-brutal
npm install
npx expo start

# To run Style 4: Ocean Magazine
cd style-4-ocean-magazine
npm install
npx expo start

# To run Style 5: Cosmic Soft
cd style-5-cosmic-soft
npm install
npx expo start
```

Press `w` in terminal to view in web browser, or scan the QR code using the Expo Go app on iOS / Android.

---

## ♿ Accessibility & Motion

- **Touch Targets:** All interactive icons, chips, and buttons have at least 44x44 pt touch targets.
- **Contrast:** Strict compliance with WCAG AA (≥ 4.5:1 text contrast).
- **Reduced Motion:** Detects device accessibility settings (`AccessibilityInfo.isReduceMotionEnabled()`) and includes an on-screen toggle (`⚡ Motion Off / ✨ Motion On`) on every build to smoothly freeze background drifts, bobs, and shine sweeps.

