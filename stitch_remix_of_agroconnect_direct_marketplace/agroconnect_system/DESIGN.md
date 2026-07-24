---
name: AgroConnect System
colors:
  surface: '#f9f9ff'
  surface-dim: '#d0daef'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff3ff'
  surface-container: '#e6eeff'
  surface-container-high: '#dee9fd'
  surface-container-highest: '#d9e3f7'
  on-surface: '#121c2a'
  on-surface-variant: '#3d4a3e'
  inverse-surface: '#273140'
  inverse-on-surface: '#ebf1ff'
  outline: '#6c7b6d'
  outline-variant: '#bbcbbb'
  surface-tint: '#006d37'
  primary: '#006d37'
  on-primary: '#ffffff'
  primary-container: '#2ecc71'
  on-primary-container: '#005027'
  inverse-primary: '#4ae183'
  secondary: '#006d37'
  on-secondary: '#ffffff'
  secondary-container: '#7bf8a1'
  on-secondary-container: '#007239'
  tertiary: '#9b4500'
  on-tertiary: '#ffffff'
  tertiary-container: '#ff995b'
  on-tertiary-container: '#733200'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#6bfe9c'
  primary-fixed-dim: '#4ae183'
  on-primary-fixed: '#00210c'
  on-primary-fixed-variant: '#005228'
  secondary-fixed: '#7efba4'
  secondary-fixed-dim: '#61de8a'
  on-secondary-fixed: '#00210c'
  on-secondary-fixed-variant: '#005228'
  tertiary-fixed: '#ffdbc9'
  tertiary-fixed-dim: '#ffb68d'
  on-tertiary-fixed: '#331200'
  on-tertiary-fixed-variant: '#763300'
  background: '#f9f9ff'
  on-background: '#121c2a'
  surface-variant: '#d9e3f7'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  title-lg:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  gutter: 24px
  margin-mobile: 16px
  margin-desktop: 32px
  sidebar-width: 280px
---

## Brand & Style

The design system is centered on the intersection of modern technology and organic agriculture. It aims to bridge the gap between rural producers and urban markets through a UI that feels **trustworthy, efficient, and grounded**.

The visual direction follows a **Modern Corporate** aesthetic with **Minimalist** influences. This ensures that complex administrative dashboards remain legible while the marketplace remains inviting. By using high-quality whitespace and a crisp, nature-inspired palette, the interface establishes professional credibility for both farmers and administrators.

### Key Tenets:
- **Clarity over Decoration:** Every element serves a functional purpose in the agricultural supply chain.
- **Organic Precision:** While the grid is strict and professional, the color palette and subtle roundedness evoke a sense of growth and freshness.
- **Information Accessibility:** High-density data (like inventory and order lists) is balanced with generous margins to prevent cognitive overload.

## Colors

The palette is rooted in the "Agro" identity, utilizing various shades of green to signify growth, health, and stability. 

- **Primary Green (#2ECC71):** Used for primary actions, success states, and brand highlights.
- **Dark Green (#27AE60):** Reserved for hover states and secondary branding to ensure contrast.
- **Light Green (#D5F4E6):** A decorative surface color for subtle alerts, category chips, or soft backgrounds in the farmer portal.
- **Accent Orange (#FF8C42):** A high-visibility color used strategically for "Urgent" notifications, "Special Offers," or primary call-to-actions that need to stand out from the green ecosystem.
- **Neutral Palette:** We use a deep slate (#374151) for primary text to maintain high readability and a cool gray (#F5F7FA) for dashboard backgrounds to separate the sidebar and cards from the main canvas.

## Typography

This design system utilizes **Inter** for its exceptional legibility in data-heavy environments. The typeface's tall x-height and neutral character make it ideal for the administrative dashboards and multi-step registration forms seen in the wireframes.

- **Headlines:** Use semi-bold weights with tight tracking to create a strong visual anchor for page titles.
- **Body Text:** Standardized at 16px for general readability, scaling down to 14px for dense table data or secondary descriptions in the product marketplace.
- **Labels:** Small, medium-weight caps are used for form field titles and table headers to distinguish them clearly from user-inputted data.

## Layout & Spacing

The layout is built on a **12-column fluid grid** for the main content area, complemented by a **fixed sidebar** for administrative and farmer dashboards.

### Layout Patterns:
- **The Sidebar:** Fixed at 280px. It uses a vertical stack for primary navigation. On mobile, the sidebar collapses into a bottom navigation bar or a hamburger drawer.
- **Grid Layouts:** Product offers in the marketplace follow a responsive card grid. On desktop, this is a 4-column layout; on tablets, 2-columns; and on mobile, a single-column list or cards.
- **Form Structures:** Forms are primarily single-column to ensure a clear completion path, grouped into logical sections (e.g., "Personal Info" vs "Agricultural Info") as seen in the registration wireframes.
- **Dashboard Density:** Use 24px gutters between dashboard widgets to maintain the "clean and modern" aesthetic while maximizing screen real estate.

## Elevation & Depth

To maintain a "clean" and "trustworthy" feel, depth is achieved through **Tonal Layering** and **Soft Ambient Shadows**. We avoid heavy skeuomorphism in favor of professional clarity.

- **Level 0 (Background):** Used for the main canvas (#F5F7FA).
- **Level 1 (Cards/Sidebar):** Pure white surfaces (#FFFFFF) with a very soft, diffused shadow (0px 4px 12px rgba(0, 0, 0, 0.05)).
- **Level 2 (Modals/Dropdowns):** Slightly more pronounced shadows to indicate temporary interaction layers (0px 8px 24px rgba(0, 0, 0, 0.1)).
- **Outlines:** In data-dense tables, we prefer subtle 1px borders (#E5E7EB) over shadows to maintain a "flat" and organized appearance.

## Shapes

The shape language is **Rounded**, reflecting the "organic" nature of the brand.

- **Components:** Standard buttons, input fields, and small cards use a 0.5rem (8px) corner radius.
- **Large Elements:** Feature cards or registration containers use a 1rem (16px) radius to feel more approachable and modern.
- **Selection States:** Active menu items in the sidebar use a "pill" shape (left-side only or full rounded) to highlight the current location without visual clutter.

## Components

### Buttons
- **Primary:** Solid #2ECC71 with white text. Rounded (8px). 
- **Secondary:** Outlined with #2ECC71 or solid Light Green (#D5F4E6) with Dark Green text.
- **Destructive:** Outlined or soft red for "Cancel" actions in forms.

### Input Fields
- **Style:** Light gray background (#F9FAFB) with a 1px border. Focus state moves the border to Primary Green.
- **Grouping:** As seen in the "Registro de Agricultor" wireframe, use distinct card containers to group related input fields.

### Cards (Product Offers)
- **Structure:** Image at the top, followed by a title, price (prominent), and a "Add to Cart" button.
- **Badges:** Use Accent Orange for "Offer" or "Discount" tags in the corner of the card.

### Tables (Admin/Dashboard)
- **Header:** Light gray background with uppercase Label-md typography.
- **Rows:** Alternating subtle backgrounds or 1px bottom borders. Action items (View, Edit) should be represented by icon buttons to save horizontal space.

### Progress Indicators
- For the 5-step registration process, use a horizontal stepper with Primary Green for completed steps and Accent Orange for the current active step.