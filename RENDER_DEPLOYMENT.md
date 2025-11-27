# Render Deployment Guide for LMS Backend

## Prerequisites
- GitHub account with your code pushed
- Render account (free tier works)
- MySQL database (you can use Render's managed database or external service)

## Step 1: Prepare Your Repository

The following files have been created for Render deployment:
- `system.properties` - Specifies Java 21 runtime
- `build.sh` - Build script for Maven
- `render.yaml` - Render service configuration (optional, can configure via dashboard)

## Step 2: Make Build Script Executable

Run this command locally before pushing:
```bash
git update-index --chmod=+x lms-backend/lms-backend/build.sh
```

Or on Linux/Mac:
```bash
chmod +x lms-backend/lms-backend/build.sh
```

## Step 3: Push to GitHub

```bash
git add .
git commit -m "Add Render deployment configuration"
git push origin main
```

## Step 4: Create Web Service on Render

1. Go to https://dashboard.render.com/
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Configure the service:

### Basic Settings:
- **Name**: lms-backend
- **Region**: Choose closest to your users
- **Branch**: main (or your default branch)
- **Root Directory**: `lms-backend/lms-backend`
- **Runtime**: Java
- **Build Command**: `./build.sh`
- **Start Command**: `java -Dserver.port=$PORT -jar target/lms-backend-0.0.1-SNAPSHOT.jar`

### Environment Variables:
Add these in the "Environment" section:

1. **SPRING_PROFILES_ACTIVE**
   - Value: `prod`

2. **DATABASE_URL**
   - Value: Your MySQL connection string
   - Format: `jdbc:mysql://HOST:PORT/DATABASE?user=USERNAME&password=PASSWORD`
   - Example: `jdbc:mysql://mysql.example.com:3306/lms_db?user=admin&password=yourpassword`

3. **JWT_SECRET**
   - Value: A secure random string (at least 32 characters)
   - Example: Generate with: `openssl rand -base64 32`

4. **JAVA_TOOL_OPTIONS** (optional, for memory management)
   - Value: `-Xmx512m -Xms256m`

### Instance Type:
- Free tier should work for testing
- Upgrade to paid tier for production

## Step 5: Database Setup

### Option A: External MySQL Database
Use services like:
- PlanetScale (free tier available)
- AWS RDS
- DigitalOcean Managed Database
- Railway

### Option B: Render PostgreSQL (Recommended)
If you want to use Render's managed database:

1. Create a PostgreSQL database on Render
2. Update `application-prod.properties` to use PostgreSQL dialect (already configured)
3. Use the internal database URL provided by Render

## Step 6: Deploy

1. Click "Create Web Service"
2. Render will automatically:
   - Clone your repository
   - Run the build script
   - Start your application
3. Monitor the logs for any errors

## Step 7: Verify Deployment

Once deployed, test your endpoints:
```bash
curl https://your-app-name.onrender.com/api/health
```

## Troubleshooting

### JAVA_HOME Error
If you see "JAVA_HOME not defined":
- Ensure `system.properties` exists with `java.runtime.version=21`
- Check that the file is in the root directory specified in Render

### Build Fails
- Check that `build.sh` is executable
- Verify Maven wrapper (`mvnw`) exists
- Check build logs for dependency issues

### Application Won't Start
- Verify all environment variables are set
- Check that DATABASE_URL is correct
- Ensure JWT_SECRET is set
- Review application logs in Render dashboard

### Database Connection Issues
- Verify database is accessible from Render's IP
- Check connection string format
- Ensure database user has proper permissions

## CORS Configuration

Your frontend URL needs to be added to CORS configuration. Update in Render environment variables or in your `WebConfig.java`:

```
FRONTEND_URL=https://your-frontend.vercel.app
```

## Health Check Endpoint

Render will automatically check if your app is running. Ensure your app responds to HTTP requests on the PORT environment variable.

## Auto-Deploy

Render automatically deploys when you push to your connected branch. To disable:
- Go to Settings → Build & Deploy → Auto-Deploy: Off

## Monitoring

- View logs: Dashboard → Your Service → Logs
- Metrics: Dashboard → Your Service → Metrics
- Set up alerts for downtime

## Cost Optimization

Free tier limitations:
- Spins down after 15 minutes of inactivity
- First request after spin-down takes 30-60 seconds
- 750 hours/month free

For production, consider paid tier for:
- No spin-down
- Better performance
- More memory/CPU
