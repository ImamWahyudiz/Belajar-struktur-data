#!/usr/bin/env python3
"""Download Piper Indonesian voice model"""

import os
import urllib.request
from pathlib import Path

# Create directory for voice models
voice_dir = Path(__file__).parent / ".piper_voices"
voice_dir.mkdir(exist_ok=True)

print("📥 Downloading Piper Indonesian voice model...")
print(f"   Directory: {voice_dir}")
print()

# URLs for Indonesian voice model (Fauzan)
base_url = "https://huggingface.co/rhasspy/piper-voices/resolve/main/id/id_ID/fauzan/medium"
files = [
    "id_ID-fauzan-medium.onnx",
    "id_ID-fauzan-medium.onnx.json"
]

for filename in files:
    url = f"{base_url}/{filename}"
    filepath = voice_dir / filename
    
    if filepath.exists():
        print(f"✅ {filename} already exists")
        continue
    
    print(f"⏬ Downloading {filename}...")
    try:
        urllib.request.urlretrieve(url, filepath)
        size_mb = filepath.stat().st_size / (1024 * 1024)
        print(f"   ✅ Downloaded ({size_mb:.1f} MB)")
    except Exception as e:
        print(f"   ❌ Error: {e}")

print()
print("✅ Voice model ready!")
print(f"   Location: {voice_dir}")
